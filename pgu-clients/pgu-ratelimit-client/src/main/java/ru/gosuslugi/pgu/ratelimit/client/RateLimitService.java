package ru.gosuslugi.pgu.ratelimit.client;

import ru.gosuslugi.pgu.dto.ratelimit.RateLimitRequest;

/**
 * Интеграция с сервисом ratelimit
 * http://pgu-dev-fednlb.test.gosuslugi.ru/ratelimit-api/swagger-ui/#/Лимиты/checkLimitUsingGET
 *
 * Установка лимитов на использование услуги
 * https://jira.egovdev.ru/browse/EPGUCORE-71987
 */
public interface RateLimitService {

    String ERROR_MESSAGE_TEXT = "Ошибка при обращении к внешнему сервису";

    void apiCheck(final String key);

    default void apiCheck(RateLimitRequest rateLimitRequest, final String key) {
        apiCheck(rateLimitRequest, key, ERROR_MESSAGE_TEXT);
    }

    default void apiCheck(RateLimitRequest rateLimitRequest, final String key, final String errorMessage) {
        apiCheck(key);
    }
}
