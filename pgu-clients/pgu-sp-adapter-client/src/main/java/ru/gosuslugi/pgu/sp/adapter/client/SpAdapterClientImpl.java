package ru.gosuslugi.pgu.sp.adapter.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.dto.SmevRequestDto;
import ru.gosuslugi.pgu.dto.SpAdapterDto;
import ru.gosuslugi.pgu.fs.common.exception.NoRightsForSendingApplicationException;
import ru.gosuslugi.pgu.sp.adapter.SpAdapterClient;
import ru.gosuslugi.pgu.common.esia.search.dto.UserPersonalData;
import ru.gosuslugi.pgu.sp.adapter.SpAdapterProperties;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class SpAdapterClientImpl implements SpAdapterClient {

    private final SpAdapterProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public SmevRequestDto createXmlAndPdf(Long orderId, Long userId, Long orgId, String requestGuid) {

        requireNonNull(orderId, "orderId is empty");
        requireNonNull(userId, "userId is empty");
        requireNonNull(requestGuid, "requestGuid is empty");

        try {
            SpAdapterDto dto = new SpAdapterDto(null, null, orderId, userId, null, requestGuid, orgId, false);
            ResponseEntity<SmevRequestDto> response = restTemplate.exchange(properties.getUrl() + "/createXmlAndPdf",
                    HttpMethod.POST,
                    new HttpEntity<>(dto),
                    SmevRequestDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

    @Override
    public byte[] getTsReportPdf(Long orderId, @Nonnull UserPersonalData userPersonalData) {

        requireNonNull(properties.getUrl(), "sp.integration.url is empty");

        try {
            var entity = new HttpEntity<>(createHeaders(userPersonalData));

            ResponseEntity<byte[]> response = restTemplate.exchange(properties.getUrl() + "pdf?orderId={orderId}&pdfName=pdf&light=true",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {},
                    Map.of("orderId", orderId));

            return response.getBody();

        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        } catch (ExternalServiceException e) {
            if (HttpStatus.FORBIDDEN.equals(e.getStatus())) {
                throw new NoRightsForSendingApplicationException("Недостаточно прав для скачивания", e);
            }
            throw e;
        }
    }

    private HttpHeaders createHeaders(UserPersonalData userPersonalData) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "acc_t=" + userPersonalData.getToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        return headers;
    }
}