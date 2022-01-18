package ru.gosuslugi.pgu.draft.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.ResourceLoader;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.draft.DraftClient;
import ru.gosuslugi.pgu.draft.config.properties.DraftServiceProperties;
import ru.gosuslugi.pgu.draft.model.DraftHolderDto;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Заглушка для сервиса черновиков, используется для локальной разоработки, когда реальный сервис недоступен.
 */
@Slf4j
@RequiredArgsConstructor
public class DraftClientStub implements DraftClient {

    private final Map<DraftKey, DraftHolderDto> drafts = new ConcurrentHashMap<>();

    private final DraftServiceProperties properties;
    private final ResourceLoader resourceLoader;

    /**
     * Возвращает черновик из ресурса указанного в свойстве mockFile (формат Spring Resource: )
     * Если файл не найден, то возвращается пустой ответ, а контроллер вернет 404
     */
    @Override
    @SneakyThrows
    public DraftHolderDto getDraftById(Long orderId, Long userId, Long orgId) {
        if(properties.getMockFile() == null) {
            DraftHolderDto draft = drafts.get(new DraftKey(orderId));

            if(draft != null) {
                if(log.isInfoEnabled()) log.info("draft was found {} for orderId {} and userId {}", draft, orderId, userId);
                return draft;
            }
        } else {

            val data = new ByteArrayOutputStream();
            try (val channel = resourceLoader.getResource(properties.getMockFile()).readableChannel()) {
                val buf = ByteBuffer.allocate(1024);
                int rd;
                while ((rd = channel.read(buf)) != -1) {
                    data.write(buf.array(), 0, rd);
                    buf.clear();
                }
            }

            val draftJson = data.toString(StandardCharsets.UTF_8);
            return JsonProcessingUtil.fromJson(draftJson, DraftHolderDto.class);

        }

        return null;
    }

    @Override
    public DraftHolderDto saveDraft(ScenarioDto scenario, String serviceId, Long userId, Long orgId, Integer draftTtl, Integer orderTtl) {
        if(log.isDebugEnabled()) log.debug("saving draft to local storage for userId {} and serviceId {} with orderId {}", userId, serviceId, scenario.getOrderId());
        DraftHolderDto draft = new DraftHolderDto();
        draft.setOrderId(scenario.getOrderId());
        draft.setOrgId(orgId);
        draft.setBody(scenario);
        draft.setLocked(false);
        draft.setType(serviceId);
        draft.setOrderTtlInSec(orderTtl);
        return draft;
    }

    @Override
    public void deleteDraft(Long orderId, Long userId) {
        if(log.isDebugEnabled()) log.debug("removing draft from local storage for orderId {}", orderId);
        drafts.remove(new DraftKey(orderId));
    }

    @Data
    class DraftKey {
        private final Long orderId;
    }
}
