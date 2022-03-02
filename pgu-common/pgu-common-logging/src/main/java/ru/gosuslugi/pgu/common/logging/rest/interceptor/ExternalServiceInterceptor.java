package ru.gosuslugi.pgu.common.logging.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;

import java.util.function.Supplier;

import static ru.gosuslugi.pgu.common.logging.util.LogUtils.flatString;
import static ru.gosuslugi.pgu.common.logging.util.LogUtils.truncate;

/**
 * Interceptor для логгирования запросов к внешним сервисам, не использующим RestTemplate.
 */
@Slf4j
public class ExternalServiceInterceptor {

    public static final String MDC_KEY_REQUEST_TIME = "request_time";

    /**
     * Максимальная длина логируемого тела запроса или ответа в байтах. Меньше нуля -- без ограничений.
     */
    private final int bodyMaxLen;

    public ExternalServiceInterceptor() {
        this(-1);
    }

    /**
     * @param bodyMaxLength максимальная длина логируемого тела запроса или ответа в байтах. Меньше нуля -- без ограничений.
     */
    public ExternalServiceInterceptor(int bodyMaxLength) {
        bodyMaxLen = bodyMaxLength;
    }

    public <T> T surroundByLogs(String serviceName, Supplier<T> func) {
        long start = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("External rest service request (custom): {}", serviceName);

            T result = func.get();

            long time = System.currentTimeMillis() - start;

            log.debug(Markers.append(MDC_KEY_REQUEST_TIME, time),
                    "External rest service response (custom): {}; body=[{}]; time: {} ms",
                    serviceName, responseToString(result), time);

            return result;
        }

        T result = func.get();
        if (log.isInfoEnabled()) {
            long time = System.currentTimeMillis() - start;

            log.info(Markers.append(MDC_KEY_REQUEST_TIME, time),
                    "External rest service response (custom): {}; body=[{}]; time: {} ms",
                    serviceName, responseToString(result), time);
        }
        return result;
    }

    public <T> T surroundByLogs(String serviceName, Supplier<T> func, String requestObject) {
        long start = System.currentTimeMillis();
        log.info("External service request (custom): {}; requestBody: {}", serviceName, requestObject);
        T result = func.get();
        long time = System.currentTimeMillis() - start;
        log.info(Markers.append(MDC_KEY_REQUEST_TIME, time),
                "External rest service response (custom): {}; body=[{}]; time: {} ms",
                serviceName, responseToString(result), time);
        return result;
    }

    private String responseToString(Object o) {
        return flatString(truncate(o == null ? "" : o.toString(), bodyMaxLen));
    }

}
