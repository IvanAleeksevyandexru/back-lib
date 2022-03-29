package ru.gosuslugi.pgu.common.kafka.properties;

import lombok.Data;
import org.apache.kafka.clients.admin.NewTopic;

/**
 * Поля конфигурация для отправки kafka сообщений
 */
@Data
public class KafkaProducerProperties {

    private String topic;

    /**
     * Number of topic partitions
     */
    private int partitions = 1;

    /**
     * Topic replication factor
     */
    private short replicationFactor = 1;


    public NewTopic toNewTopic() {
        return new NewTopic(topic, partitions, replicationFactor);
    }

}
