package ru.gosuslugi.pgu.common.kafka.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackageClasses = KafkaBatchProcessor.class)
public class KafkaBatchProcessorConfig {

    private final KafkaBatchProcessorProperties kafkaBatchProcessorProperties;

    @Bean
    public ExecutorService batchKafkaExecutorService() {
        var threadFactory = new BasicThreadFactory.Builder()
            .namingPattern(kafkaBatchProcessorProperties.getThreadPrefixName() + "-%d")
            .build();
        return Executors.newFixedThreadPool(kafkaBatchProcessorProperties.getThreads(), threadFactory);
    }

}
