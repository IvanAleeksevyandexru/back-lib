package ru.gosuslugi.pgu.common.kafka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.gosuslugi.pgu.common.kafka")
public class KafkaEpguAutoConfiguration {
}
