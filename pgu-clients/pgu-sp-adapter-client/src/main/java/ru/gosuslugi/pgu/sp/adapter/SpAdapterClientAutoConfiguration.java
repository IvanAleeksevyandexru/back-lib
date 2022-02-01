package ru.gosuslugi.pgu.sp.adapter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.sp.adapter.client.SpAdapterClientImpl;
import ru.gosuslugi.pgu.sp.adapter.client.SpAdapterClientStub;

@Configuration
@EnableConfigurationProperties(SpAdapterProperties.class)
public class SpAdapterClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "sp.integration.enabled", matchIfMissing = true)
    public SpAdapterClient spAdapterClient(RestTemplate restTemplate, SpAdapterProperties properties) {
        return new SpAdapterClientImpl(properties, restTemplate);
    }

    @Bean
    @ConditionalOnProperty(value = "sp.integration.enabled", havingValue = "false")
    public SpAdapterClient spAdapterClientStub(ResourceLoader resourceLoader, SpAdapterProperties properties) {
        return new SpAdapterClientStub(resourceLoader, properties);
    }
}