package ru.gosuslugi.pgu.common.core.exception.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.gosuslugi.pgu.common.core.exception.handler")
public class GlobalExceptionHandlerAutoConfiguration {

}
