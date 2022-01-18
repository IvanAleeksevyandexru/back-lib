package ru.gosuslugi.pgu.common.logging.config;

import brave.Tracer;
import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import ru.gosuslugi.pgu.common.logging.annotation.aop.LoggingMethodInterceptor;
import ru.gosuslugi.pgu.common.logging.annotation.aop.LoggingPointcutAdvisor;
import ru.gosuslugi.pgu.common.logging.filter.TraceFilter;
import ru.gosuslugi.pgu.common.logging.rest.RequestResponseLogger;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.ExternalServiceInterceptor;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.LogInterceptor;
import ru.gosuslugi.pgu.common.logging.service.SpanService;
import ru.gosuslugi.pgu.common.logging.util.LogUtils;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@ComponentScan("ru.gosuslugi.pgu.common.logging")
public class LoggingAutoConfiguration {

    private final ConfigurableEnvironment env;

    @PostConstruct
    public void logPropertiesOnStartup() {
        new ConfigParameterLogger(env).printActiveProperties();
    }

    @Bean
    public LoggingMethodInterceptor loggingMethodInterceptor() {
        return new LoggingMethodInterceptor();
    }

    @Bean
    public LoggingPointcutAdvisor loggingPointcutAdvisor() {
        return new LoggingPointcutAdvisor(loggingMethodInterceptor());
    }

    @Bean
    public RequestResponseLogger requestResponseLogger() {
        return new RequestResponseLogger();
    }

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor(requestResponseLogger());
    }

    @Bean
    public TraceFilter traceFilter(Tracer tracer) {
        return new TraceFilter(tracer);
    }

    @Bean
    public SpanService spanService(Tracer tracer) {
        return new SpanService(tracer, externalServiceInterceptor());
    }

    @Bean
    public ExternalServiceInterceptor externalServiceInterceptor() {
        return new ExternalServiceInterceptor(LogUtils.parseLoggingMaxBodyLen(env));
    }

    @Bean
    public SpanHandler spanHandlerForAddingEnv(@Value("${spring.application.env:empty}") String env) {
        return new SpanHandler() {
            @Override
            public boolean begin(TraceContext context, MutableSpan span, TraceContext parent) {
                span.tag("env", env);
                return true;
            }
        };
    }

}
