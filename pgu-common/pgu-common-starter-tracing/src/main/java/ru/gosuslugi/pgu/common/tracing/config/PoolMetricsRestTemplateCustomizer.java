package ru.gosuslugi.pgu.common.tracing.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.httpcomponents.PoolingHttpClientConnectionManagerMetricsBinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.interceptor.creator.RestTemplateNameHolder;
import ru.gosuslugi.pgu.common.core.util.ReflectionUtils;

/**
 * Класс добавляет метрики для пулов в Apache Http Client, который создается при создании RestTemplate
 */
@Slf4j
@RequiredArgsConstructor
public class PoolMetricsRestTemplateCustomizer implements RestTemplateCustomizer {

    private final MeterRegistry registry;

    @Override
    public void customize(RestTemplate restTemplate) {
        String name = RestTemplateNameHolder.getName(restTemplate);
        if (name == null) {
            log.info("Not found name for rest template, pool metrics ignored");
            return;
        }
        try {
            var connectionManager = (PoolingHttpClientConnectionManager)
                    ReflectionUtils.readNestedField(restTemplate, "requestFactory", "httpClient", "connManager");

            new PoolingHttpClientConnectionManagerMetricsBinder(connectionManager, name + "-pool").bindTo(registry);
            log.info("Pool metrics activated for restTemplate {}", name);
        } catch (Exception e) {
            log.info("Error on metrics instrumentation on restTemplate '{}': {}", name, e.getMessage());
        }
    }
}
