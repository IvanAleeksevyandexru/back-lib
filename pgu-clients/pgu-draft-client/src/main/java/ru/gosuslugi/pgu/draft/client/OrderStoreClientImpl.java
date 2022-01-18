package ru.gosuslugi.pgu.draft.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.draft.OrderStoreClient;
import ru.gosuslugi.pgu.draft.config.properties.OrderStoreServiceProperties;

import java.util.Collections;

import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
public class OrderStoreClientImpl implements OrderStoreClient {

    private static final String API_PATH = "/internal/api/sp-store/v1/{id}";

    private final RestTemplate restTemplate;
    private final OrderStoreServiceProperties properties;

    @Override
    public String getOrderXmlById(Long orderId) {
        return callStoreService(orderId, HttpMethod.GET, null);
    }

    @Override
    public String saveOrderXml(Long orderId, String xmlString) {
        return callStoreService(orderId, HttpMethod.PUT, xmlString);
    }

    @Override
    public void deleteOrderXml(Long orderId) {
        callStoreService(orderId, HttpMethod.DELETE, null);
    }

    private String callStoreService(Long orderId, HttpMethod httpMethod, String body) {
        requireNonNull(orderId, "orderId is empty");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(properties.getUrl() + API_PATH, httpMethod, new HttpEntity<>(body, httpHeaders), String.class, orderId);
            return responseEntity.getBody();
        } catch (EntityNotFoundException e) {
            return null;
        } catch (PguException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalServiceException("Order store client: Error on " + httpMethod + " Xml by orderId: " + orderId + "; " + e.getMessage(), e);
        }
    }
}
