package ru.gosuslugi.pgu.terrabyte.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.interceptor.creator.RestTemplateCreator;
import ru.gosuslugi.pgu.terrabyte.client.TerrabyteClient;
import ru.gosuslugi.pgu.terrabyte.client.service.TerrabyteClientImpl;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TerrabyteClientProperties.class)
@ConditionalOnProperty("terrabyte.client.storage-url")
public class TerrabyteAutoConfiguration {


    @Bean
    TerrabyteClient terrabyteClient(TerrabyteClientProperties properties,
                                    ObjectMapper objectMapper,
                                    List<HttpMessageConverter<?>> messageConverters,
                                    ConfigurableEnvironment env,
                                    RestTemplateCustomizer... customizers) {

        RestTemplate restTemplate = RestTemplateCreator.create("terrabyte-client", objectMapper, env, customizers);
        restTemplate.setMessageConverters(messageConverters);
        return new TerrabyteClientImpl(restTemplate, properties);
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }

    @Bean
    public FormHttpMessageConverter formHttpMessageConverter() {
        return new FormHttpMessageConverter();
    }

}