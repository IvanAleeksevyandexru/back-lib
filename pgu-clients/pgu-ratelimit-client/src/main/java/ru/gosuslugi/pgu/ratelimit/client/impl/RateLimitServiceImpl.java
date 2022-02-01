package ru.gosuslugi.pgu.ratelimit.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.dto.ratelimit.RateLimitRequest;
import ru.gosuslugi.pgu.ratelimit.client.RateLimitService;
import ru.gosuslugi.pgu.ratelimit.client.config.RateLimitProperties;
import ru.gosuslugi.pgu.ratelimit.client.exception.RateLimitServiceException;

import java.util.Map;

@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {

    private static final String VERSION = "v1";

    private static final String API_PATH = "/api-check?key={key}&lim={limit}&ttl={ttl}";

    private final RestTemplate restTemplate;

    private final RateLimitProperties rateLimitProperties;

    @Override
    public void apiCheck(final String key) {
        var rateLimitRequest = new RateLimitRequest();
        rateLimitRequest.setVersion(VERSION);
        rateLimitRequest.setTtl(rateLimitProperties.getTtl());
        rateLimitRequest.setLimit(rateLimitProperties.getLimit());
        apiCheck(rateLimitRequest, key);
    }

    @Override
    public void apiCheck(RateLimitRequest rateLimitRequest, final String key, final String errorMessage) {

        try {
            // 200 -OK; 400 - Некорректный запрос; 429 - Превышен лимит; 500 - Внутренняя ошибка
            restTemplate.exchange(
                    rateLimitProperties.getPguUrl() + "/" + rateLimitRequest.getVersionOrDefault(VERSION) + API_PATH,
                    HttpMethod.GET,
                    null,
                    Void.class,
                    Map.of(
                            "key", key,
                            "limit", rateLimitRequest.getLimitOrDefault(rateLimitProperties.getLimit()),
                            "ttl", rateLimitRequest.getTtlOrDefault(rateLimitProperties.getTtl())
                    )
            );
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw new RateLimitServiceException(errorMessage, rateLimitRequest.getLimit(), e);
        } catch (ExternalServiceException e) {
            if (e.getStatus() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new RateLimitServiceException(errorMessage, rateLimitRequest.getLimit(), e);
            }
            throw new ExternalServiceException(e);
        } catch (RestClientException | EntityNotFoundException e) {
            throw new ExternalServiceException(e);
        }
    }
}
