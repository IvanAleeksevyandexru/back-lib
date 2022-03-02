package ru.gosuslugi.pgu.common.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducerCreator {

    @Value(value = "${spring.kafka.brokers}")
    private String brokers;

    public <K, V> DefaultKafkaProducerFactory<K, V> createProducerFactory(
            Serializer<K> keySerializer,
            Serializer<V> valueSerializer
    ) {
        return createProducerFactory(keySerializer, valueSerializer, Map.of());
    }

    public <K, V> DefaultKafkaProducerFactory<K, V> createProducerFactory(
            Serializer<K> keySerializer,
            Serializer<V> valueSerializer,
            Map<String, Object> configProps
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.putAll(configProps);
        return new DefaultKafkaProducerFactory<>(props, keySerializer, valueSerializer);
    }

    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(
            ProducerFactory<K, V> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }


}
