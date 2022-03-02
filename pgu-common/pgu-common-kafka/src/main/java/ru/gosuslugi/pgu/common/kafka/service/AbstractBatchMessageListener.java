package ru.gosuslugi.pgu.common.kafka.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.util.StopWatch;
import ru.atc.carcass.common.exception.FaultException;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;
import static ru.gosuslugi.pgu.common.kafka.util.KafkaTraceUtils.runInSpan;

@Slf4j
public abstract class AbstractBatchMessageListener<K, V> implements BatchMessageListener<K, V> {

    protected final KafkaConsumerProperties consumerProperties;
    protected final SpanService spanService;
    protected BiConsumer<V, String> onErrorCallback;

    private final ExecutorService executorService;

    protected AbstractBatchMessageListener(KafkaConsumerProperties consumerProperties, SpanService spanService) {
        var threadFactory = new BasicThreadFactory.Builder()
                .namingPattern(consumerProperties.getThreadPrefixName() + "-%d")
                .build();
        this.executorService = Executors.newFixedThreadPool(consumerProperties.getProcessingThreads(), threadFactory);
        this.consumerProperties = consumerProperties;
        this.spanService = spanService;
    }

    protected abstract void processMessage(V message);

    @Override
    public void onMessage(List<ConsumerRecord<K, V>> messages) {
        debug(log, () -> String.format("Process %d messages from topic %s.", messages.size(), getTopicName(messages)));

        StopWatch watch = new StopWatch("[Batch Messages Processing]");
        watch.start();

        processBatch(messages);

        watch.stop();
        debug(log, watch::shortSummary);
    }

    private Function<ConsumerRecord<K, V>, Callable<V>> toExecutionCallback() {
        return msg -> (Callable<V>) () -> {
            runInSpan(spanService,"kafka-batch-message-processing", msg.headers(), () -> processMessage(msg.value()));
            return msg.value();
        };
    }

    private void processBatch(List<ConsumerRecord<K, V>> messages) {
        try {
            List<Callable<V>> executionCallbacks = messages.stream()
                    .map(toExecutionCallback())
                    .collect(Collectors.toList());

            List<Future<V>> futures = executorService
                    .invokeAll(executionCallbacks, consumerProperties.getExecuteThreadWaitTimeSec(), TimeUnit.SECONDS);
            checkProcessingResult(messages, futures);
        } catch (InterruptedException e) {
            throw new FaultException(e);
        }
    }

    private void checkProcessingResult(List<ConsumerRecord<K, V>> messages, List<Future<V>> futures) {
        for (int i = 0; i < futures.size(); i++) {
            try {
                futures.get(i).get();
            } catch (Exception e) {
                ConsumerRecord<K, V> unprocessedMessage = messages.get(i);
                runInSpan(spanService, "kafka-process-error", unprocessedMessage.headers(),
                        () -> {
                            String errorMsg = String.format("Unprocessed kafka message: %s; Reason: %s", unprocessedMessage.value(), e.getMessage());
                            if (log.isErrorEnabled()) {
                                log.error(errorMsg, e);
                            }
                            if (onErrorCallback != null) {
                                onErrorCallback.accept(unprocessedMessage.value(), errorMsg);
                            }
                        }
                );
            }
        }
    }

    private String getTopicName(List<ConsumerRecord<K, V>> messages) {
        if (messages.size() > 0) {
            return messages.get(0).topic();
        }
        return null;
    }
}
