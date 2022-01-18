package ru.gosuslugi.pgu.common.logging.rest;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Класс для логгирования запросов и ответов REST контроллеров
 */
@Slf4j
public class RequestResponseLogger {

    private final List<String> ignoredUrls = List.of("/actuator");

    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (log.isDebugEnabled()) {
            if (isUrlIgnored(httpServletRequest.getRequestURI())) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, String> parameters = buildParametersMap(httpServletRequest);

            stringBuilder.append("Service request: ");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

            if (!parameters.isEmpty()) {
                stringBuilder.append("parameters=[").append(parameters).append("] ");
            }

            if (body != null) {
                stringBuilder.append("body=[").append(body).append("]");
            }

            log.debug(stringBuilder.toString());
        }
    }

    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        if (log.isDebugEnabled()) {
            if (isUrlIgnored(httpServletRequest.getRequestURI())) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Service response ");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("code=[").append(httpServletResponse.getStatus()).append("] ");
            stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
            stringBuilder.append("responseBody=[").append(body).append("] ");

            log.debug(stringBuilder.toString());
        }
    }

    private boolean isUrlIgnored(String requestURI) {
        return ignoredUrls.stream()
                .anyMatch(url -> requestURI.toLowerCase().contains(url));
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }

        return map;
    }
}
