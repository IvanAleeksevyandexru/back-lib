package ru.gosuslugi.pgu.common.tracing.config;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import ru.gosuslugi.pgu.common.core.file.factory.YamlPropertyLoaderFactory;

@Slf4j
@Configuration
@PropertySource(value = "classpath:zipkin.yml", factory = YamlPropertyLoaderFactory.class)
@PropertySource(value = "classpath:prometheus.yml", factory = YamlPropertyLoaderFactory.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class TracingAutoConfiguration {

    @Bean
    public RestTemplateCustomizer poolMetricsRestTemplateCustomizer(MeterRegistry registry) {
        return new PoolMetricsRestTemplateCustomizer(registry);
    }
}
