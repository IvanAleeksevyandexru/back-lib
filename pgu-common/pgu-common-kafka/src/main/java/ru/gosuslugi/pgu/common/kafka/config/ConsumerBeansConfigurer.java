package ru.gosuslugi.pgu.common.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;

import java.util.HashMap;
import java.util.Map;

public class ConsumerBeansConfigurer {

    public static Map<String, Object> createProps(String brokers, KafkaConsumerProperties properties) {
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

    public static void configureContainerFactory(ConcurrentKafkaListenerContainerFactory<?, ?> factory, KafkaConsumerProperties properties) {

        factory.setConcurrency(properties.getConcurrency());

        if (properties.isBatchProcessing()) {
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);
            factory.setBatchListener(true);
            return;
        }
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        factory.setBatchListener(false);
    }
}
