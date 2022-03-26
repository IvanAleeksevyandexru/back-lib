package ru.gosuslugi.pgu.common.core.interceptor.creator;

import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Класс хранит имя, с которым был создан restTemplate.
 * Это имя используется для поиска параметров соединения при вызове RestTemplateFactory.get(name, ...
 * Дальше оно требуется в PoolMetricsRestTemplateCustomizer, но т.к. сохранить его в RestTemplate негде,
 * используется данный промежуточный класс.
 */
public class RestTemplateNameHolder {

    private static Map<RestTemplate, String> nameHolder = new ConcurrentHashMap<>();

    public static void registerRestTemplate(RestTemplate restTemplate, String name) {
        nameHolder.put(restTemplate, name);
    }

    public static String getName(RestTemplate restTemplate) {
        return nameHolder.get(restTemplate);
    }
}
