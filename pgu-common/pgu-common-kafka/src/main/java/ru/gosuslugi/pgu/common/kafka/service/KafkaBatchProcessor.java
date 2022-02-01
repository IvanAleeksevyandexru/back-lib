package ru.gosuslugi.pgu.common.kafka.service;

import brave.propagation.B3SingleFormat;
import brave.propagation.TraceContextOrSamplingFlags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.atc.carcass.common.exception.FaultException;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.error;

/**
 * Класс, для обработки сообщений Kafka пачками.
 * <ul>
 *     <li>Вычитывает пачку сообщений.</li>
 *     <li>Перекладывает обработку в пулл полотоков.</li>
 *     <li>В случае если в одном из потоков произошла ошибка сообщения, то оно пересылается в соответствующий DLT топик.</li>
 *     <li>Коммитися вся пачка ранее прочитанных сообщений.</li>
 * </ul>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBatchProcessor {

    private final KafkaBatchProcessorProperties kafkaBatchProcessorProperties;
    private final ExecutorService batchKafkaExecutorService;
    private final SpanService spanService;

    private <T> Function<Message<T>, Callable<T>> toExecutionCallback(Consumer<T> handler) {
        return msg -> (Callable<T>) () -> {
            runInSpan("kafka-message-processing", msg.getHeaders(), () -> handler.accept(msg.getPayload()));
            return msg.getPayload();
        };
    }

    public <T> void process(List<Message<T>> messages, Consumer<T> handler) {
        debug(log, () -> String.format("Process %d messages.", messages.size()));

        StopWatch watch = new StopWatch("[Batch Messages Processing]");
        watch.start();

        processBatch(messages, handler);

        watch.stop();
        debug(log, watch::shortSummary);
    }

    private <T> void processBatch(List<Message<T>> messages, Consumer<T> handler) {
        try {
            List<Callable<T>> executionCallbacks = messages.stream()
                    .map(toExecutionCallback(handler))
                    .collect(Collectors.toList());

            List<Future<T>> futures = batchKafkaExecutorService
                    .invokeAll(executionCallbacks, kafkaBatchProcessorProperties.getExecuteThreadWaitTimeSec(), TimeUnit.SECONDS);
            checkProcessingResult(messages, futures);
        } catch (InterruptedException e) {
            throw new FaultException(e);
        }
    }

    private <T> void checkProcessingResult(List<Message<T>> messages, List<Future<T>> futures) {
        for (int i = 0; i < futures.size(); i++) {
            try {
                futures.get(i).get();
            } catch (Exception e) {
                Message<T> unprocessedMessage = messages.get(i);
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
            spanService.runInNewTrace(spanName, traceContext, () -> {
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
