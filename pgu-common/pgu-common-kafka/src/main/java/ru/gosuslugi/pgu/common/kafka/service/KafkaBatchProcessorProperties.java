package ru.gosuslugi.pgu.common.kafka.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka.batch-processor")
public class KafkaBatchProcessorProperties {

    int threads = 1;
    long executeThreadWaitTimeSec = 600;
    String threadPrefixName = "kafka-consumer";

}
