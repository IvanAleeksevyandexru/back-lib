package ru.gosuslugi.pgu.draft.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.handler.RestResponseErrorHandler;
import ru.gosuslugi.pgu.common.core.interceptor.creator.RestTemplateCreator;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.draft.DraftClient;
import ru.gosuslugi.pgu.draft.client.DraftClientImpl;
import ru.gosuslugi.pgu.draft.client.DraftClientStub;
import ru.gosuslugi.pgu.draft.config.properties.DraftServiceProperties;

@Configuration
@EnableConfigurationProperties(DraftServiceProperties.class)
public class DraftClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "draft-client.enabled", matchIfMissing = true)
    public DraftClient draftClient(DraftServiceProperties properties, ConfigurableEnvironment env, RestTemplateCustomizer... customizers) {
        var restTemplate = RestTemplateCreator.create("draft-client", JsonProcessingUtil.getObjectMapper(), env, customizers);
        restTemplate.setErrorHandler(new RestResponseErrorHandler());

        return new DraftClientImpl(restTemplate, properties);
    }

    @Bean
    @ConditionalOnProperty(value = "draft-client.enabled", havingValue = "false")
    public DraftClient draftClientStub(DraftServiceProperties properties, ResourceLoader resourceLoader) {
        return new DraftClientStub(properties, resourceLoader);
    }

}
