package ru.gosuslugi.pgu.ratelimit.client.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.ratelimit.client.RateLimitService;
import ru.gosuslugi.pgu.ratelimit.client.impl.RateLimitServiceImpl;
import ru.gosuslugi.pgu.ratelimit.client.impl.RateLimitServiceStub;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "ratelimit.enabled", havingValue = "true")
    public RateLimitService rateLimitService(RestTemplate restTemplate, RateLimitProperties rateLimitProperties) {
        return new RateLimitServiceImpl(restTemplate, rateLimitProperties);
    }

    @Bean
    @ConditionalOnProperty(value = "ratelimit.enabled", havingValue = "false", matchIfMissing = true)
    public RateLimitService rateLimitServiceStub() {
        return new RateLimitServiceStub();
    }
}
