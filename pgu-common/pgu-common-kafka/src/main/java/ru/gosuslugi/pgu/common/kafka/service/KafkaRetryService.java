package ru.gosuslugi.pgu.common.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static java.util.Objects.isNull;

/**
 * Сервис обеспечивает отправку сообщения, обработанного с ошибкой,
 * в специальный топик для последующей переотправки.
 * Самой переотправкой занимается отдельное приложение.
 *
 * Описание решения тут: https://confluence.egovdev.ru/pages/viewpage.action?pageId=220487270
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaRetryService {

    public static final String RETRY_TOPIC_HEADER_NAME = "X-Retry-Topic";
    public static final String ORIG_TOPIC_HEADER_NAME = "X-Retry-OrigTopic";
    public static final String RETRY_PARTITION_KEY_HEADER_NAME = "X-Retry-PartitionKey";
    public static final String HEADER_LAST_RETRY = "X-Last-Retry";

    private final KafkaTemplate<String, String> retryKafkaTemplate;
    private final DefaultKafkaHeaderMapper defaultKafkaHeaderMapper;


    public void pushToRetryTopic(KafkaConsumerProperties consumerProperties,
                                 ConsumerRecord<?, ?> data,
                                 Runnable onRetryFailedFunc) {
        if (isNull(consumerProperties.getRetryTopic())) {
            return;
        }
        String payloadAsString = JsonProcessingUtil.toJson(data.value());

        Map<String, Object> messageHeaders = new HashMap<>();
        defaultKafkaHeaderMapper.toHeaders(data.headers(), messageHeaders);

        if (messageHeaders.containsKey(HEADER_LAST_RETRY)) {
            log.info("Kafka retry failed. Send message to dlq");
            onRetryFailedFunc.run();
        }
        messageHeaders.put(ORIG_TOPIC_HEADER_NAME, consumerProperties.getTopic());
        messageHeaders.put(RETRY_PARTITION_KEY_HEADER_NAME, data.partition());

        String topicName = Optional.ofNullable(data.headers().lastHeader(RETRY_TOPIC_HEADER_NAME))
                .map(Header::value)
                .map(String::new)
                .orElse(consumerProperties.getRetryTopic());
        messageHeaders.put(KafkaHeaders.TOPIC, topicName);

        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, payloadAsString);
        defaultKafkaHeaderMapper.fromHeaders(new MessageHeaders(messageHeaders), record.headers());

        try {
            log.info("Send message for retry to topic {}: {}", topicName, record);
            retryKafkaTemplate.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error on sending to retry topic: " + e.getMessage(), e);
        }
    }

}
