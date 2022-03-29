package ru.gosuslugi.pgu.common.kafka.config;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.gosuslugi.pgu.common.kafka.properties.AbstractConsumerProps;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class AbstractConsumerConfig<K,V> {

    @Value(value = "${spring.kafka.brokers}")
    private String brokers;

    protected <T> ConsumerFactory<K, T> getDefaultKafkaFactory(AbstractConsumerProps commonConsumerProps, Class clazz){
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, commonConsumerProps.getGroupId());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, commonConsumerProps.getMaxPollRecords());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        if (commonConsumerProps.getPollTimeout() != null) {
            consumerProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, (int) commonConsumerProps.getPollTimeout().toMillis());
        }
        if (commonConsumerProps.getFetchMaxSize() != null) {
            consumerProps.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, commonConsumerProps.getFetchMaxSize());
        }
        if (commonConsumerProps.getPartitionFetchBytes() != null) {
            consumerProps.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, commonConsumerProps.getPartitionFetchBytes());
        }

        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.gosuslugi.pgu.dto");

        return new DefaultKafkaConsumerFactory(consumerProps,
                new ErrorHandlingDeserializer<>(new LongDeserializer()),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(clazz)));
    }

    protected ConcurrentKafkaListenerContainerFactory<K,V> getKafkaListener(ConsumerFactory<K, V> factory){
        ConcurrentKafkaListenerContainerFactory<K, V> concurrentFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        concurrentFactory.setConsumerFactory(factory);
        return concurrentFactory;
    }

    protected <T> ConcurrentKafkaListenerContainerFactory<K,T> getAdditionalKafkaListener(ConsumerFactory<K, T> factory){
        ConcurrentKafkaListenerContainerFactory<K, T> concurrentFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        concurrentFactory.setConsumerFactory(factory);
        return concurrentFactory;
    }


}
