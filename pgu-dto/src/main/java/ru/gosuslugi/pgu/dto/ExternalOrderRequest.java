package ru.gosuslugi.pgu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * Запрос на создание ордера по шаблону
 */
@Data
public class ExternalOrderRequest {

    @Schema(description = "Фактическая цель услуги")
    String targetId;

    @Schema(description = "Id услуги")
    String serviceId;

    @Schema(description = "Id заявления")
    Long orderId;

    @Schema(description = "Ответы пользователя для инициализации услуги")
    Map<String, Object> answers;
}
