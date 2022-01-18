package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Поля конфигурация для отправки kafka сообщений
 */
@Getter
@Setter
@ToString
@Validated
public class KafkaProducerProperties {

    private boolean enabled = false;

    private TopicProperties targetTopic;

    @Getter
    @Setter
    @ToString
    @Validated
    public static class TopicProperties {

        /**
         * Name of topic
         */
        @NotEmpty
        private String topicName;

        /**
         * Number of topic partitions
         */
        private int topicPartitions = 1;

        /**
         * Topic replication factor
         */
        private short topicReplicationFactor = 1;
    }
}
