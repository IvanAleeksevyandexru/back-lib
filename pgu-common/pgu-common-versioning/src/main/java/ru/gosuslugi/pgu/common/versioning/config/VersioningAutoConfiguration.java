package ru.gosuslugi.pgu.common.versioning.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.gosuslugi.pgu.common.versioning.dto.RequestEnvironment;
import ru.gosuslugi.pgu.common.versioning.filter.VersionFilter;

@Configuration
@RequiredArgsConstructor
@ComponentScan("ru.gosuslugi.pgu.common.versioning")
public class VersioningAutoConfiguration {

    private final RequestEnvironment requestEnvironment;

    @Value("${spring.application.env}")
    private String appEnv;

    @Bean
    public VersionFilter versionFilter() {
        return new VersionFilter(appEnv, requestEnvironment);
    }
}
