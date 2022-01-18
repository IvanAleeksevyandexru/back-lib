package ru.gosuslugi.pgu.common.core.interceptor.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;
import ru.atc.carcass.common.spring.RestTemplateFactory;
import ru.gosuslugi.pgu.common.core.exception.handler.RestResponseErrorHandler;
import ru.gosuslugi.pgu.common.core.interceptor.model.RestClientHeadersInterceptor;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.common.logging.rest.interceptor.RemoteRestInterceptor;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.info;
import static ru.gosuslugi.pgu.common.logging.util.LogUtils.parseLoggingMaxBodyLen;

@Slf4j
public class RestTemplateCreator {
    /** Общее максимальное количество подключений по всем HTTP-роутам */
    private static final int MAX_TOTAL_CONNECTIONS = 10000;
    /** Максимальное количество подключений для каждого HTTP-роута в пуле */
    private static final int MAX_ROUTE_CONNECTIONS = 1200;

    public static RestTemplate create(String configPrefix, ObjectMapper objectMapper, ConfigurableEnvironment env, RestTemplateCustomizer... customizers) {
        return create(configPrefix, objectMapper, env, null, customizers);
    }

    public static RestTemplateBuilder create(RestTemplateBuilder restTemplateBuilder, ConfigurableEnvironment env,
                                             RestTemplateCustomizer... customizers) {
        return restTemplateBuilder
                .customizers(customizers)
                .messageConverters(RestTemplateCreator.createMessageConverters(JsonProcessingUtil.getObjectMapper()))
                .interceptors(RestTemplateCreator.createRestInterceptors(env))
                .errorHandler(new RestResponseErrorHandler());
    }

    public static RestTemplate createOrDefault(RestTemplateBuilder restTemplateBuilder, int timeout, RestTemplate defaultRestTemplate) {
        if (timeout == -1) {
            return defaultRestTemplate;
        }
        Duration connectTimeout = Duration.of(timeout, ChronoUnit.MILLIS);
        return restTemplateBuilder
                .requestFactory(() -> RestTemplateCreator.createConnectionFactory(timeout, timeout, -1))
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(connectTimeout)
                .build();
    }

    public static ClientHttpRequestFactory createConnectionFactory(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        info(log, () -> String.format("HttpComponentsClientHttpRequestFactory init with timeouts: connectTimeout=%d, socketTimeout=%d, connectionRequestTimeout=%d", connectTimeout, socketTimeout, connectionRequestTimeout));
        HttpClient httpClient = createHttpClient(connectTimeout, socketTimeout, connectionRequestTimeout);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(socketTimeout);
        requestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        return requestFactory;
    }

    /**
     * Создаёт объект {@link HttpClient} с заданными таймаутами, а также созданным пулом потоков.
     * @param connectTimeout           максимальное время в мс для установления соединения с удаленным хостом/сервером
     * @param socketTimeout            максимальный промежуток времени в мс между 2 последовательными пакетами данных при передаче данных с сервера на клиент
     * @param connectionRequestTimeout максимальное время в мс для ожидания получения соединения от менеджера соединений/пула потоков в HttpClient
     * @return конфигурация
     */
    private static HttpClient createHttpClient(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        info(log, () -> String.format("HttpClient init with timeouts: connectTimeout=%d, socketTimeout=%d, connectionRequestTimeout=%d", connectTimeout, socketTimeout, connectionRequestTimeout));
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = poolingConnectionManager();
        RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec("ignoreCookies").build();
        RequestConfig requestConfig = getTimeoutRequestConfig(connectTimeout, socketTimeout, connectionRequestTimeout);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(customizedRequestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    private static PoolingHttpClientConnectionManager poolingConnectionManager() {
        SocketConfig socketConfig = SocketConfig.custom().build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
        return poolingHttpClientConnectionManager;
    }

    /**
     * Создаёт объект конфигурации для {@link HttpClient} с заданными таймаутами.
     * @param connectTimeout           максимальное время в мс для установления соединения с удаленным хостом/сервером
     * @param socketTimeout            максимальный промежуток времени в мс между 2 последовательными пакетами данных при передаче данных с сервера на клиент
     * @param connectionRequestTimeout максимальное время в мс для ожидания получения соединения от менеджера соединений/пула потоков в HttpClient
     * @return конфигурация
     */
    public static RequestConfig getTimeoutRequestConfig(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectTimeout(connectTimeout);
        requestConfigBuilder.setSocketTimeout(socketTimeout);
        requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
        RequestConfig requestConfig = requestConfigBuilder.build();
        return requestConfig;
    }

    public static RestTemplate create(String configPrefix, ObjectMapper objectMapper, ConfigurableEnvironment env, UriTemplateHandler handler,
                                      RestTemplateCustomizer... customizers) {
        copyApplicationPropertiesToSystem(env, configPrefix);

        List<HttpMessageConverter<?>> messageConverters = createMessageConverters(objectMapper);

        RestTemplate restTemplate = RestTemplateFactory.get(configPrefix, messageConverters);
        restTemplate.getInterceptors().add(createConfiguredRestHeadersInterceptor(env));
        restTemplate.getInterceptors().add(createConfiguredRemoteRestInterceptor(env));
        if (nonNull(handler)) {
            restTemplate.setUriTemplateHandler(handler);
        }
        if (customizers != null) {
            for (RestTemplateCustomizer customizer : customizers) {
                customizer.customize(restTemplate);
            }
        }
        return restTemplate;
    }

    private static List<HttpMessageConverter<?>> createMessageConverters(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        byteArrayHttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.setSupportedMediaTypes(List.of(MediaType.MULTIPART_FORM_DATA));
        return List.of(stringHttpMessageConverter,
                mappingJackson2HttpMessageConverter,
                byteArrayHttpMessageConverter,
                formHttpMessageConverter);
    }

    private static List<ClientHttpRequestInterceptor> createRestInterceptors(ConfigurableEnvironment env) {
        return List.of(
                createConfiguredRestHeadersInterceptor(env),
                createConfiguredRemoteRestInterceptor(env)
        );
    }

    private static RemoteRestInterceptor createConfiguredRemoteRestInterceptor(ConfigurableEnvironment env) {
        int bodyMaxLen = parseLoggingMaxBodyLen(env);
        return bodyMaxLen > 0 ? new RemoteRestInterceptor(bodyMaxLen) : new RemoteRestInterceptor();
    }

    private static RestClientHeadersInterceptor createConfiguredRestHeadersInterceptor(ConfigurableEnvironment env){
        return new RestClientHeadersInterceptor(env);
    }

    /**
     * Копирует параметры, начинающиеся с prefix, из application.yml в системные properties.
     * Это нужно, т.к. RestTemplateFactory берет их из System.getProperty()
     */
    private static void copyApplicationPropertiesToSystem(ConfigurableEnvironment env, String prefix) {
        env.getPropertySources().stream()
                .filter(it -> it instanceof MapPropertySource && it.getName().contains("applicationConfig"))
                .flatMap(it -> Stream.of(((MapPropertySource) it).getPropertyNames()))
                .filter(key -> key.startsWith(prefix))
                .forEach(key -> {
                    if (System.getProperty(key) == null) {
                        System.setProperty(key, env.getProperty(key));
                    }
                });
    }
}
