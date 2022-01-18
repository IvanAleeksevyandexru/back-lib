package ru.gosuslugi.pgu.draft;

import ru.gosuslugi.pgu.draft.model.DraftHolderDto;
import ru.gosuslugi.pgu.dto.ScenarioDto;

public interface DraftClient {

    /**
     * Получение черновика их внешнего сервиса
     * @param orderId - номер заявки
     * @param userId - id пользователя
     * @param orgId - id организации
     * @return {@code null}, если черновик не найден
     */
    DraftHolderDto getDraftById(Long orderId, Long userId, Long orgId);


    /**
     *
     * @param scenario - сценарий
     * @param serviceId - id сервиса
     * @param userId - id пользователя
     * @param orgId - id организации
     * @param draftTtl - время храния черновиков (в сутках)
     * @return Черновик
     */
    DraftHolderDto saveDraft(ScenarioDto scenario, String serviceId, Long userId, Long orgId, Integer draftTtl, Integer orderTtl);

    /**
     *
     * @param orderId - номер заявки
     * @param userId - id пользователя
     */
    void deleteDraft(Long orderId, Long userId);
}
