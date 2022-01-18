package ru.gosuslugi.pgu.common.esia.search.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.esia.search.service.UddiService;

import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractSearchService {

    protected static final String UDDI_INTERNAL_ESIA = "uddi:gosuslugi.ru:services:internal/esia";

    @Value("${esia.system.token.host}")
    protected String systemTokenHost;

    protected final RestTemplate restTemplate;

    protected final UddiService uddiService;

    public AbstractSearchService(RestTemplate restTemplate,
                                 UddiService uddiService) {
        this.restTemplate = restTemplate;
        this.uddiService = uddiService;
    }

    protected <T> T sendRequest(String path, Map<String, String> parameters, String scope, Class<T> responseClass) {
        String token = receiveToken(scope);

        String endpoint = uddiService.getEndpoint(UDDI_INTERNAL_ESIA);

        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(endpoint + path,
                    HttpMethod.GET,
                    new HttpEntity<String>(this.prepareSecurityHeader(token)),
                    new ParameterizedTypeReference<>() {
                    },
                    parameters
            );
            T responseBody = responseEntity.getBody();
            if (log.isDebugEnabled()) log.debug("Response from ESIA:\n {}", responseBody);

            return responseBody;
        } catch (EntityNotFoundException e) {
            if (log.isWarnEnabled()) log.warn("ESIA find user service [ {} ] return code : 404. parameters:  {}", path, parameters);
            return null;
        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

    protected String receiveToken(String scope) {
        UriComponents uri = UriComponentsBuilder.fromUriString(systemTokenHost)
                .queryParam("scope", scope).build(false);
        try {
            return restTemplate.getForObject(uri.toUriString(), String.class);
        } catch(RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

    protected HttpHeaders prepareSecurityHeader(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("Authorization", List.of("Bearer " + token));
        return httpHeaders;
    }
}
