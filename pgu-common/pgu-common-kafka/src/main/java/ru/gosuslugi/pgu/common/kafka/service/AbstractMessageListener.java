package ru.gosuslugi.pgu.common.kafka.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import ru.gosuslugi.pgu.common.core.exception.RetryKafkaException;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.util.function.BiConsumer;

import static ru.gosuslugi.pgu.common.kafka.util.KafkaTraceUtils.runInSpan;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageListener<K, V> implements MessageListener<K, V> {

    protected final KafkaConsumerProperties consumerProperties;
    protected final KafkaRetryService kafkaRetryService;
    protected final SpanService spanService;
    protected BiConsumer<V, String> onErrorCallback;

    protected abstract void processMessage(V message);

    @Override
    public void onMessage(ConsumerRecord<K, V> data) {
        runInSpan(spanService,"kafka-message-processing", data.headers(), () -> {
            log.info("Получено сообщение из kafka очереди {}: {}", data.topic(), data);
            try {
                processMessage(data.value());
            } catch (RetryKafkaException e) {
                log.error("Error on processing kafka message. Send to retry topic. {}", e.getMessage(), e);
                kafkaRetryService.pushToRetryTopic(consumerProperties, data,
                        () -> { if (onErrorCallback != null) onErrorCallback.accept(data.value(), e.getMessage()); });
            } catch (Exception e) {
                String errorMsg = String.format("Error on processing kafka message: %s: %s", data.value(), e.getMessage());
                log.error(errorMsg, e);
                if (onErrorCallback != null) {
                    onErrorCallback.accept(data.value(), errorMsg);
                }
            }
        });
    }

}
