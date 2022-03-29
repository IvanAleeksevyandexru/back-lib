package ru.gosuslugi.pgu.common.kafka.util;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;
import java.util.Map;

public class KafkaFactoryUtils {


    public static  <K, V> DefaultKafkaProducerFactory<K, V> createDefaultProducerFactory(
        String brokers,
        Serializer<K> keySerializer,
        Serializer<V> valueSerializer
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        return new DefaultKafkaProducerFactory<>(props, keySerializer, valueSerializer);
    }
}
