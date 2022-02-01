package ru.gosuslugi.pgu.draft.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.common.logging.annotation.Log;
import ru.gosuslugi.pgu.draft.DraftClient;
import ru.gosuslugi.pgu.draft.config.properties.DraftServiceProperties;
import ru.gosuslugi.pgu.draft.model.DraftHolderDto;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.util.Collections;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Log
@Slf4j
@RequiredArgsConstructor
public class DraftClientImpl implements DraftClient {

    private final String MASTER_OID_ATTR_NAME = "masterOid";
    private final RestTemplate restTemplate;
    private final DraftServiceProperties properties;

    @Override
    public DraftHolderDto getDraftById(Long orderId, Long userId, Long orgId) {
        requireNonNull(orderId, "orderId is empty");
        requireNonNull(userId, "userId is empty");
        try {
            ResponseEntity<DraftHolderDto> entity = restTemplate.exchange(
                    properties.getUrl() + "/internal/api/drafts/v3/{id}",
                    HttpMethod.GET,
                    new HttpEntity<>(tokenToHeaders(userIdToToken(userId, orgId))),
                    DraftHolderDto.class,
                    orderId
            );
            if (entity.getBody() != null) {
                checkDraftOwner(entity.getBody(), orderId, userId, orgId);
            }
            return entity.getBody();
        } catch (EntityNotFoundException e) {
            return null;
        } catch (PguException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Draft Service Client: Error on get draft by orderId: " + orderId
                    + "; " + e.getMessage(), e);
        }
    }

    @Override
    public DraftHolderDto saveDraft(ScenarioDto scenario, String serviceId, Long userId, Long orgId, Integer draftTtl, Integer orderTtl) {
        requireNonNull(scenario, "scenario is empty");
        requireNonNull(serviceId, "serviceId is empty");

        DraftHolderDto saveDraftDto = new DraftHolderDto();
        saveDraftDto.setOrderId(scenario.getOrderId());
        saveDraftDto.setBody(scenario);
        saveDraftDto.setType(serviceId);
        saveDraftDto.setTtlInSec(Optional.ofNullable(draftTtl).map(DraftClientImpl::toSecondsFromDays).orElse(null));
        saveDraftDto.setOrderTtlInSec(Optional.ofNullable(orderTtl).map(DraftClientImpl::toSecondsFromDays).orElse(null));
        try {
            ResponseEntity<DraftHolderDto> entity = restTemplate.exchange(
                    properties.getUrl() + "/internal/api/drafts/v3/{id}",
                    HttpMethod.PUT,
                    new HttpEntity<>(saveDraftDto, tokenToHeaders(userIdToToken(userId, orgId))),
                    DraftHolderDto.class,
                    scenario.getOrderId()
            );
            return entity.getBody();
        } catch (PguException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Draft Service Client: Error on save draft by orderId: " + scenario.getOrderId()
                    + "; " + e.getMessage(), e);
        }
    }

    static int toSecondsFromDays(int days) {
        return days * 24 * 60 * 60;
    }

    @Override
    public void deleteDraft(Long orderId, Long userId) {
        requireNonNull(orderId, "orderId is empty");
        requireNonNull(userId, "userId is empty");
        try {
            restTemplate.exchange(
                    properties.getUrl() + "/internal/api/drafts/v3/{id}",
                    HttpMethod.DELETE,
                    new HttpEntity<>(null, tokenToHeaders(userIdToToken(userId, null))),
                    Void.class,
                    orderId
            );
        } catch (RestClientException e) {
            throw new ExternalServiceException("Draft Service Client: Error on delete draft by orderId: " + orderId
                    + "; " + e.getMessage(), e);
        }
    }

    private String userIdToToken(Long userId, Long orgId) {
        return userId + "@" + (orgId == null ? "" : orgId.toString()) + "@@@1";
    }

    private HttpHeaders tokenToHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("token", token);
        return headers;
    }

    private void checkDraftOwner(DraftHolderDto draft, long orderId, long userId, Long orgId) {
        if ( (draft.getOrgId() == null || orgId == null) && draft.getOid() != null) {
            checkDraftOwnerUser(determineRealOwnerId(draft.getOid(), draft.getBody()), determineRealOwnerId(userId, draft.getBody()), orderId);
        } else {
            checkDraftOwnerOrg(draft.getOrgId(), orgId, orderId);
        }
    }

    private Long determineRealOwnerId(Long draftOid, ScenarioDto draft) {
        if(draftOid < 0) {
            if(draft.getAdditionalParameters().containsKey(MASTER_OID_ATTR_NAME)) {
                try {
                    draftOid = Long.parseLong(draft.getAdditionalParameters().get(MASTER_OID_ATTR_NAME));
                } catch (NumberFormatException e) {
                    val message = String.format("Значение, указывающее собственника черновика %s, повреждено. Не возможно продолжить. Значение = %s",
                            draft.getOrderId(), draft.getAdditionalParameters().get(MASTER_OID_ATTR_NAME));
                    throw new SecurityException(message);
                }
            }
        }
        return draftOid;
    }

    private void checkDraftOwnerOrg(Long ownerOrgId, Long orgId, long orderId) {
        if (ownerOrgId != null && orgId != null && ownerOrgId.longValue() != orgId.longValue()) {
            val message = String.format("Пользователь не сотрудник компании-владелеца черновика orgId = %s, draft.orgId = %s, orderId = %s",
                    orgId, ownerOrgId, orderId);
            throw new SecurityException(message);
        }
    }

    private void checkDraftOwnerUser(long ownerId, long userId, long orderId) {
        if (ownerId != userId) {
            val message = String.format("Пользователь не владелец черновика userId = %s, draft.uid = %s, orderId = %s",
                    userId, ownerId, orderId);
            throw new SecurityException(message);
        }
    }
}
