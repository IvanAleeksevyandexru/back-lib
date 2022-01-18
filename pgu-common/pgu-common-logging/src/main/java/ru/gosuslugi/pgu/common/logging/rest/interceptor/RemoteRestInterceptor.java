package ru.gosuslugi.pgu.common.logging.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ru.gosuslugi.pgu.common.logging.rest.interceptor.ExternalServiceInterceptor.MDC_KEY_REQUEST_TIME;
import static ru.gosuslugi.pgu.common.logging.util.LogUtils.flatString;
import static ru.gosuslugi.pgu.common.logging.util.LogUtils.truncateAndConvertToStr;

/**
 * Interceptor для логгирования запросов к внешним сервисам.
 * Должен инжектиться в RestTemplate.
 */
@Slf4j
public class RemoteRestInterceptor implements ClientHttpRequestInterceptor {
    /**
     * Максимальная длина логируемого тела запроса или ответа в байтах. Меньше нуля -- без ограничений.
     */
    private final int bodyMaxLen;

    public RemoteRestInterceptor() {
        this(-1);
    }

    /**
     * @param bodyMaxLength максимальная длина логируемого тела запроса или ответа в байтах. Меньше нуля -- без ограничений.
     */
    public RemoteRestInterceptor(int bodyMaxLength) {
        bodyMaxLen = bodyMaxLength;
    }

    @Override
    public ClientHttpResponse intercept(
            final HttpRequest request,
            final byte[] reqBody, ClientHttpRequestExecution ex
    ) throws IOException {
        if (log.isDebugEnabled()) {
            final long start = System.currentTimeMillis();

            log.debug(getRequestMessage(request, truncateAndConvertToStr(reqBody, bodyMaxLen)));

            final ClientHttpResponse debugResponse = ex.execute(request, reqBody);

            final byte[] bytes = getBytes(debugResponse);

            long time = System.currentTimeMillis() - start;

            log.debug(Markers.append(MDC_KEY_REQUEST_TIME, time),
                    getResponseMessage(request, debugResponse, bytes, time));

            return generateProxyClientHttpResponse(debugResponse, bytes);
        }

        long start = System.currentTimeMillis();
        ClientHttpResponse response = ex.execute(request, reqBody);
        if (log.isInfoEnabled()) {
            long time = System.currentTimeMillis() - start;

            log.info(
                    Markers.append(MDC_KEY_REQUEST_TIME, time),
                    "External rest service response {} {} time: {} ms",
                    request.getMethod(),
                    request.getURI(),
                    time
            );
        }
        return response;
    }

    private byte[] getBytes(ClientHttpResponse debugResponse) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(debugResponse.getBody(), bos);
        return bos.toByteArray();
    }

    private String getResponseMessage(HttpRequest request, ClientHttpResponse response, byte[] bodyBytes, long time) {
        try {
            return "External rest service response " +
                    "method=[" + request.getMethod() + "] " +
                    "path=[" + request.getURI() + "] " +
                    "code=[" + response.getRawStatusCode() + "] " +
                    "responseHeaders=[" + response.getHeaders() + "] " +
                    "responseBody=[" + flatString(truncateAndConvertToStr(bodyBytes, bodyMaxLen)) + "] " +
                    "time: " + time + " ms";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRequestMessage(HttpRequest request, String body) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("External rest service request: ");
        stringBuilder.append("method=[").append(request.getMethod()).append("] ");
        stringBuilder.append("path=[").append(request.getURI()).append("] ");
        stringBuilder.append("headers=[").append(request.getHeaders()).append("] ");

        if (body != null) {
            stringBuilder.append("body=[").append(flatString(body)).append("]");
        }

        return stringBuilder.toString();
    }

    private ClientHttpResponse generateProxyClientHttpResponse(final ClientHttpResponse debugResponse,
                                                               final byte[] bytes) {
        return new ByteClientHttpResponse(debugResponse, bytes);
    }
}
