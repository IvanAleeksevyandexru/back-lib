package ru.gosuslugi.pgu.common.logging.rest.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteClientHttpResponse implements ClientHttpResponse {

    private final ClientHttpResponse debugResponse;
    private final byte[] bytes;

    public ByteClientHttpResponse(ClientHttpResponse debugResponse, byte[] bytes) {
        this.debugResponse = debugResponse;
        this.bytes = bytes;
    }

    @Override
    public HttpHeaders getHeaders() {
        return debugResponse.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return debugResponse.getStatusCode();
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return debugResponse.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return debugResponse.getStatusText();
    }

    @Override
    public void close() {
        debugResponse.close();
    }
}
