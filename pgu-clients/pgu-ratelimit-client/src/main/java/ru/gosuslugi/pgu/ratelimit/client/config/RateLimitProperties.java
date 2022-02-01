package ru.gosuslugi.pgu.ratelimit.client.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitProperties {

    private String pguUrl;
    // Период времени, в течение, которого считаются обращения пользователя к услуге. Измеряется в секундах
    private Long ttl;
    // Максимальное количество обращений за контролируемый период времени
    private Long limit;
}
