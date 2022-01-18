package ru.gosuslugi.pgu.sd.storage.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.logging.annotation.Log;
import ru.gosuslugi.pgu.dto.descriptor.ServiceDescriptor;
import ru.gosuslugi.pgu.sd.storage.ServiceDescriptorClient;
import ru.gosuslugi.pgu.sd.storage.config.ServiceDescriptorClientProperties;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Log(printResult = false)
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"cacheServiceDescriptor"})
public class ServiceDescriptorClientImpl implements ServiceDescriptorClient {

    public static final String TEMPLATES_API_PATH = "/v1/templates/{serviceId}";
    public static final String DESCRIPTOR_API_PATH = "/v1/scenario/{serviceId}";
    private final RestTemplate restTemplate;
    private final ServiceDescriptorClientProperties properties;

    @Override
    @Cacheable(sync = true)
    public String getServiceDescriptor(String serviceId) {
        return callStoreService(serviceId, String.class, DESCRIPTOR_API_PATH);
    }

    @Override
    public ByteBuffer getTemplatePackage(String serviceId) {
        return callStoreService(serviceId, ByteBuffer.class, TEMPLATES_API_PATH);
    }

    @Override
    public String getTemplateRefreshTime(String serviceId) {
        return callStoreService(serviceId, String.class, TEMPLATES_API_PATH + "/refresh");
    }

    @Override
    @CacheEvict(key="#serviceId")
    public void saveServiceDescriptor(String serviceId, ServiceDescriptor serviceDescriptor) {
        try {
            restTemplate.exchange(
                    properties.getUrl() + DESCRIPTOR_API_PATH,
                    HttpMethod.PUT, new HttpEntity<>(serviceDescriptor, getHeaders()), String.class, serviceId);
        } catch (Exception e) {
            throw new ExternalServiceException("ServiceDescriptor store client: Error on save service descriptor by serviceId: " + serviceId + "; " + e.getMessage(), e);
        }
    }

    @Override
    @CacheEvict(key="#serviceId")
    public void clearCachedDescriptor(String serviceId) {
    }

    private <T> T callStoreService(String serviceId, Class<T> responseType, String apiPath) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(properties.getUrl() + apiPath, HttpMethod.GET, new HttpEntity<>(null, getHeaders()), responseType, serviceId);
            return responseEntity.getBody();
        } catch (EntityNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new ExternalServiceException("ServiceDescriptor store client: Error on " + apiPath + " by serviceId: " + serviceId + "; " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, String> getTemplatePackageChecksums(Collection<String> serviceIds) {
        if (Objects.isNull(serviceIds) || serviceIds.isEmpty()) return Collections.emptyMap();
        try {
            return restTemplate.postForObject(properties.getUrl() + "/v1/templates/checksums", serviceIds, Map.class);
        } catch (Exception e) {
            throw new ExternalServiceException("ServiceDescriptor store client: Error on get service-template package checksums by serviceIds: \"" + serviceIds + "\". Details: " + e.getMessage(), e);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }
}
