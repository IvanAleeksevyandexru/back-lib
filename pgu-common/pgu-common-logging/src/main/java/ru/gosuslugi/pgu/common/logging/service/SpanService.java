package ru.gosuslugi.pgu.common.logging.service;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.ExternalServiceInterceptor;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Сервис позволяет запускать произвольный код внутри нового Span для правильного логгирования в zipkin.
 * Предпочтительно использовать аннотацию @NewSpan, но т.к. ее можно вешать только на публичный метод сервиса,
 * в крайнем случае используется этот сервис.
 */
public class SpanService {

    public static final String USER_ID_TAG = "userId";
    public static final String ORDER_ID_TAG = "orderId";
    public static final String SERVICE_CODE_TAG = "serviceCode";
    public static final String EXCEPTION_TAG = "exception";
    public static final String MESSAGE_TAG = "message";

    private final Tracer tracer;
    private final ExternalServiceInterceptor externalServiceInterceptor;

    public SpanService(Tracer tracer, ExternalServiceInterceptor externalServiceInterceptor) {
        this.tracer = tracer;
        this.externalServiceInterceptor = externalServiceInterceptor;
    }

    public <T> T runInNewSpan(String spanName, Supplier<T> func) {
        return runInNewSpan(spanName, func, Map.of());
    }

    public <T> T runInNewSpan(String spanName, Supplier<T> func, Map<String, String> tags) {
        Span newSpan = tracer.nextSpan().name(spanName);
        tags.entrySet()
                .forEach(entry -> newSpan.tag(entry.getKey(), entry.getValue()));
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            return func.get();
        } finally {
            newSpan.finish();
        }
    }

    public String getCurrentTraceId() {
        Span span = tracer.currentSpan();
        if (span != null) {
            return span.context().traceIdString();
        }
        return null;
    }

    public void addTagToSpan(String tag, String message) {
        Span span = tracer.currentSpan();
        span.tag(tag, message);
    }

    public <T> T runInNewTrace(String spanName, TraceContextOrSamplingFlags context, Supplier<T> func) {
        Span newSpan = tracer.nextSpan(context).name(spanName);
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            return func.get();
        } finally {
            newSpan.finish();
        }
    }

    /**
     * Метода для запуска внешнего сервиса, не использующего restTemplate.
     * Оборачивает вызов в span, логирует старт и окончание, считает время выполнения
     */
    public <T> T runExternalService(String serviceName, String spanName, Supplier<T> func, Map<String, String> tags) {
        Span newSpan = tracer.nextSpan().name(spanName);
        tags.entrySet()
                .forEach(entry -> newSpan.tag(entry.getKey(), entry.getValue()));
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            return externalServiceInterceptor.surroundByLogs(serviceName, func);
        } finally {
            newSpan.finish();
        }
    }

}
