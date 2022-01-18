package ru.gosuslugi.pgu.draft.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.draft.DraftClient;
import ru.gosuslugi.pgu.draft.client.DraftClientImpl;
import ru.gosuslugi.pgu.draft.client.DraftClientStub;
import ru.gosuslugi.pgu.draft.config.properties.DraftServiceProperties;

@Configuration
@EnableConfigurationProperties(DraftServiceProperties.class)
public class DraftClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "draft-client.enabled", matchIfMissing = true)
    public DraftClient draftClient(RestTemplate restTemplate, DraftServiceProperties properties) {
        return new DraftClientImpl(restTemplate, properties);
    }

    @Bean
    @ConditionalOnProperty(value = "draft-client.enabled", havingValue = "false")
    public DraftClient draftClientStub(DraftServiceProperties properties, ResourceLoader resourceLoader) {
        return new DraftClientStub(properties, resourceLoader);
    }

}
