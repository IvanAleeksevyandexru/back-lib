package ru.gosuslugi.pgu.draft.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.draft.OrderStoreClient;
import ru.gosuslugi.pgu.draft.client.OrderStoreClientImpl;
import ru.gosuslugi.pgu.draft.client.OrderStoreClientStub;
import ru.gosuslugi.pgu.draft.config.properties.OrderStoreServiceProperties;

@Configuration
@EnableConfigurationProperties(OrderStoreServiceProperties.class)
public class OrderStoreClientAutoConfiguration {

    @Bean
    @ConditionalOnProperty(value = "order-store-client.enabled", havingValue = "true")
    public OrderStoreClient orderStoreClient(RestTemplate restTemplate, OrderStoreServiceProperties properties) {
        return new OrderStoreClientImpl(restTemplate, properties);
    }

    @Bean
    @ConditionalOnProperty(value = "order-store-client.enabled", havingValue = "false", matchIfMissing = true)
    public OrderStoreClient orderStoreClientStub(OrderStoreServiceProperties properties) {
        return new OrderStoreClientStub();
    }
}
