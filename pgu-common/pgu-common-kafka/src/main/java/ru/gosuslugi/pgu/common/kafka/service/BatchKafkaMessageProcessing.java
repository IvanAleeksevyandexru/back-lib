package ru.gosuslugi.pgu.common.kafka.service;

import brave.propagation.B3SingleFormat;
import brave.propagation.TraceContextOrSamplingFlags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.StopWatch;
import ru.atc.carcass.common.exception.FaultException;
import ru.gosuslugi.pgu.common.kafka.properties.KafkaConsumerProperties;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.error;

/**
 * Базовый класс, для обработки сообщений Kafka пачками.
 * <ul>
 *     <li>Вычитывает пачку сообщений.</li>
 *     <li>Перекладывает обработку в пулл полотоков.</li>
 *     <li>В случае если в одном из потоков произошла ошибка сообщения, то оно пересылается в соответствующий DLT топик.</li>
 *     <li>Коммитися вся пачка ранее прочитанных сообщений.</li>
 * </ul>
 */
@Slf4j
public abstract class BatchKafkaMessageProcessing<MT> implements InitializingBean {


    private ExecutorService executorService;

    protected abstract KafkaConsumerProperties getKafkaFormServiceProperties();
    protected abstract SpanService getSpanService();

    protected abstract void processMessage(MT message);

    @Override
    public void afterPropertiesSet() {
        var threadFactory = new BasicThreadFactory.Builder()
                .namingPattern(getKafkaFormServiceProperties().getThreadPrefixName() + "-%d")
                .build();
        executorService = Executors.newFixedThreadPool(
                getKafkaFormServiceProperties().getProcessingThreads(),
                threadFactory);
    }

    protected Function<Message<MT>, Callable<MT>> toExecutionCallback() {
        return msg -> (Callable<MT>) () -> {
            runInSpan("kafka-message-processing", msg.getHeaders(), () -> processMessage(msg.getPayload()));
            return msg.getPayload();
        };
    }

    public void process(List<Message<MT>> messages) {
        debug(log, () -> String.format("Process %d messages.", messages.size()));

        StopWatch watch = new StopWatch("[Batch Messages Processing]");
        watch.start();

        processBatch(messages);

        watch.stop();
        debug(log, watch::shortSummary);
    }

    private void processBatch(List<Message<MT>> messages) {
        try {
            List<Callable<MT>> executionCallbacks = messages.stream()
                    .map(toExecutionCallback())
                    .collect(Collectors.toList());

            List<Future<MT>> futures = executorService
                    .invokeAll(executionCallbacks, getKafkaFormServiceProperties().getExecuteThreadWaitTimeSec(), TimeUnit.SECONDS);
            checkProcessingResult(messages, futures);
        } catch (InterruptedException e) {
            throw new FaultException(e);
        }
    }

    private void checkProcessingResult(List<Message<MT>> messages, List<Future<MT>> futures) {
        for (int i = 0; i < futures.size(); i++) {
            try {
                futures.get(i).get();
            } catch (Exception e) {
                Message<MT> unprocessedMessage = messages.get(i);
                runInSpan("kafka-process-error", unprocessedMessage.getHeaders(),
                        () -> error(log, () -> String.format("Unprocessed kafka message: %s; Reason: %s; Stacktrace: %s",
                                unprocessedMessage.getPayload(), e.getMessage(),
                                Arrays.toString(e.getStackTrace()))
                        )
                );
            }
        }
    }

    private void runInSpan(String spanName, MessageHeaders headers, Runnable fn) {
        TraceContextOrSamplingFlags traceContext = getTraceContext(headers);
        if (traceContext == null) {
            fn.run();
        } else {
            getSpanService().runInNewTrace(spanName, traceContext, () -> {
                fn.run();
                return null;
            });
        }
    }

    private TraceContextOrSamplingFlags getTraceContext(MessageHeaders headers) {
        byte[] b3 = (byte[]) headers.get("b3");
        return b3 == null ? null : B3SingleFormat.parseB3SingleFormat(new String(b3, StandardCharsets.UTF_8));
    }
}