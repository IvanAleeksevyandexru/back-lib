package ru.gosuslugi.pgu.common.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.GenericMessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaConsumerCreator {

    @Value(value = "${spring.kafka.brokers}")
    private String brokers;

    public <K, V> ConsumerFactory<K, V> createConsumerFactory(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            KafkaConsumerProperties consumerProperties
    ) {
        return new DefaultKafkaConsumerFactory<>(
                createProps(consumerProperties),
                new ErrorHandlingDeserializer<>(keyDeserializer),
                new ErrorHandlingDeserializer<>(valueDeserializer)
        );
    }

    public <K, V> ConcurrentMessageListenerContainer<K, V> createListenerContainer(
            ConsumerFactory<K, V> consumerFactory,
            KafkaConsumerProperties consumerProperties,
            GenericMessageListener messageListener
    ) {
        ContainerProperties containerProps = new ContainerProperties(consumerProperties.getTopic());
        containerProps.setMessageListener(messageListener);
        if (consumerProperties.isBatchProcessing()) {
            containerProps.setAckMode(ContainerProperties.AckMode.BATCH);
        } else {
            containerProps.setAckMode(ContainerProperties.AckMode.RECORD);
        }
        var container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProps);
        container.setConcurrency(consumerProperties.getConcurrency());
        return container;
    }

    private Map<String, Object> createProps(KafkaConsumerProperties properties) {
        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaxPollRecords());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        if (properties.getPollTimeout() != null) {
            consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) properties.getPollTimeout().toMillis());
        }
        if (properties.getFetchMaxSize() != null) {
            consumerProps.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, properties.getFetchMaxSize());
        }
        if (properties.getPartitionFetchBytes() != null) {
            consumerProps.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, properties.getPartitionFetchBytes());
        }

        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.gosuslugi.pgu.dto");

        return consumerProps;
    }
}
