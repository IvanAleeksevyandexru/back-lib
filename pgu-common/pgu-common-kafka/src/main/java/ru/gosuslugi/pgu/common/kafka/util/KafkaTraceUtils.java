package ru.gosuslugi.pgu.common.kafka.util;

import brave.propagation.B3SingleFormat;
import brave.propagation.TraceContextOrSamplingFlags;
import lombok.experimental.UtilityClass;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import java.nio.charset.StandardCharsets;

@UtilityClass
public class KafkaTraceUtils {

    public static void runInSpan(SpanService spanService, String spanName, Headers headers, Runnable fn) {
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


    public static TraceContextOrSamplingFlags getTraceContext(Headers headers) {
        Header b3Header = headers.lastHeader("b3");
        return b3Header == null ? null : B3SingleFormat.parseB3SingleFormat(new String(b3Header.value(), StandardCharsets.UTF_8));
    }

}
