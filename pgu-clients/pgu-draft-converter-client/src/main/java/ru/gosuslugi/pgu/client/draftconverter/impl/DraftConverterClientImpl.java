package ru.gosuslugi.pgu.client.draftconverter.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.client.draftconverter.DraftConverterClient;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.interceptor.creator.RestTemplateCreator;
import ru.gosuslugi.pgu.dto.ExternalOrderRequest;
import ru.gosuslugi.pgu.dto.ScenarioDto;
import ru.gosuslugi.pgu.dto.XmlCustomConvertRequest;
import ru.gosuslugi.pgu.dto.XmlDraftConvertRequest;

import java.util.LinkedHashMap;
import java.util.Map;

public class DraftConverterClientImpl implements DraftConverterClient {

    private static final String EXTERNAL_ORDER_REQUEST_PATH = "/services/convert/externalOrder";
    private static final String XML_DRAFT_REQUEST_PATH = "/services/{serviceId}/convert";
    private static final String CUSTOM_XML_DRAFT_REQUEST_PATH = "/services/convert/custom";

    private final String draftConverterUrl;
    private final RestTemplateBuilder restTemplateBuilder;
    private final RestTemplate defaultRestTemplate;

    public DraftConverterClientImpl(String draftConverterUrl,
                                    RestTemplateBuilder restTemplateBuilder,
                                    RestTemplate restTemplate,
                                    ConfigurableEnvironment env,
                                    RestTemplateCustomizer... customizers) {
        this.draftConverterUrl = draftConverterUrl;
        this.defaultRestTemplate = restTemplate;
        this.restTemplateBuilder = RestTemplateCreator.create(restTemplateBuilder, env, customizers);
    }

    @Override
    public ScenarioDto convertExternalOrderRequest(ExternalOrderRequest request) {
        try {
            ResponseEntity<ScenarioDto> responseEntity = defaultRestTemplate.exchange(draftConverterUrl + EXTERNAL_ORDER_REQUEST_PATH,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    ScenarioDto.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

    @Override
    public ScenarioDto convertXmlDraft(XmlDraftConvertRequest request, int timeout) {
        RestTemplate restTemplate = RestTemplateCreator.createOrDefault(restTemplateBuilder, timeout, defaultRestTemplate);
        try {
            ResponseEntity<ScenarioDto> responseEntity = restTemplate.exchange(draftConverterUrl + XML_DRAFT_REQUEST_PATH,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    ScenarioDto.class,
                    Map.of("serviceId", request.getServiceId())
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }

    @Override
    public ScenarioDto convertXmlDraft(XmlDraftConvertRequest request) {
        return convertXmlDraft(request, -1);
    }

    @Override
    public LinkedHashMap convertXmlCustom(XmlCustomConvertRequest request) {
        try {
            return defaultRestTemplate.exchange(draftConverterUrl + CUSTOM_XML_DRAFT_REQUEST_PATH,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    LinkedHashMap.class
            ).getBody();
        } catch (RestClientException e) {
            throw new ExternalServiceException(e);
        }
    }
}
