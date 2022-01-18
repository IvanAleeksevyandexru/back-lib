package ru.gosuslugi.pgu.client.draftconverter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.client.draftconverter.DraftConverterClient;
import ru.gosuslugi.pgu.client.draftconverter.impl.DraftConverterClientImpl;
import ru.gosuslugi.pgu.client.draftconverter.impl.DraftConverterClientStub;

@Configuration
@EnableConfigurationProperties(DraftConverterClientProperties.class)
public class DraftConverterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "draft-converter.integration.enabled", havingValue = "true")
    public DraftConverterClient draftConverterClient(DraftConverterClientProperties properties,
                                                     RestTemplate restTemplate,
                                                     RestTemplateBuilder restTemplateBuilder,
                                                     ConfigurableEnvironment env,
                                                     RestTemplateCustomizer... customizers) {
        return new DraftConverterClientImpl(properties.getUrl(), restTemplateBuilder, restTemplate, env, customizers);
    }

    @Bean
    @ConditionalOnProperty(value = "draft-converter.integration.enabled", havingValue = "false", matchIfMissing = true)
    public DraftConverterClient draftConverterClientStub() {
        return new DraftConverterClientStub();
    }
}
