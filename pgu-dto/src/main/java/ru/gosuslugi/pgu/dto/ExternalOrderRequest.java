package ru.gosuslugi.pgu.dto;

import lombok.Data;

import java.util.Map;

/**
 * Запрос на создание ордера по шаблону
 */
@Data
public class ExternalOrderRequest {
    String targetId;
    String serviceId;
    Long orderId;
    Map<String, Object> answers;
}
