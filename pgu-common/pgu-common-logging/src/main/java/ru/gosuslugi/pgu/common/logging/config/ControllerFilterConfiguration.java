package ru.gosuslugi.pgu.common.logging.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.LogInterceptor;

@Configuration
public class ControllerFilterConfiguration implements WebMvcConfigurer {

    private final LogInterceptor logInterceptor;

    @Autowired
    public ControllerFilterConfiguration(LogInterceptor logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }

}
