package ru.gosuslugi.pgu.common.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = "ru.gosuslugi.pgu.common.kafka")
public class KafkaEpguAutoConfiguration {

    private final KafkaProducerCreator kafkaProducerCreator;

    @Bean
    public ProducerFactory<String, String> retryProducerFactory() {
        return kafkaProducerCreator.createProducerFactory(
                new StringSerializer(),
                new StringSerializer()
        );
    }

    @Bean
    public KafkaTemplate<String, String> retryKafkaTemplate() {
        return kafkaProducerCreator.createKafkaTemplate(retryProducerFactory());
    }

    @Bean
    public DefaultKafkaHeaderMapper defaultKafkaHeaderMapper() {
        return new DefaultKafkaHeaderMapper();
    }

}
