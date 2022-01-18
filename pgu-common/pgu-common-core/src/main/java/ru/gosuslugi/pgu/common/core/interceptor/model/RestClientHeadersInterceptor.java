package ru.gosuslugi.pgu.common.core.interceptor.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.ByteClientHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * interceptor для добавления header-a
 * User-Agent с указанием версии и имени приложения,
 * которое вызывает иное приложения.
 */
@Slf4j
@RequiredArgsConstructor
public class RestClientHeadersInterceptor implements ClientHttpRequestInterceptor {

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String USER_AGENT = "user-agent";

    private final Environment environment;
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var userAgent = request.getHeaders().get(USER_AGENT);
        if(Objects.isNull(userAgent)){
            userAgent = new ArrayList<>();
        }
        userAgent.add(this.getUserAgentHeader(request.getHeaders()));
        request.getHeaders().put(USER_AGENT, userAgent);
        final ClientHttpResponse debugResponse = execution.execute(request, body);
        final byte[] bytes = getBytes(debugResponse);
        return generateProxyClientHttpResponse(debugResponse, bytes);
    }

    private String getUserAgentHeader(HttpHeaders headers){

        String result = environment.getProperty("spring.application.name");
        String version = environment.getProperty("info.app.version");
        if(Objects.isNull(result)){
            throw new PguException("Не указан user-agent для клиента");
        }
        if(Objects.nonNull(version)){
            result = result.concat("/").concat(version);
        }
        var traceId = headers.get(TRACE_ID);
        if(Objects.nonNull(traceId) && !traceId.isEmpty()){
            result = result.concat(" - ").concat(traceId.get(0));
        }
        return result;
    }

    private byte[] getBytes(ClientHttpResponse debugResponse) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(debugResponse.getBody(), bos);
        return bos.toByteArray();
    }

    private ClientHttpResponse generateProxyClientHttpResponse(final ClientHttpResponse debugResponse,
                                                               final byte[] bytes) {
        return new ByteClientHttpResponse(debugResponse, bytes);
    }
}
