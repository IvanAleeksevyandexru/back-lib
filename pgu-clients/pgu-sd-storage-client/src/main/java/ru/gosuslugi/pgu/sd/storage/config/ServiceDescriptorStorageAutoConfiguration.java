package ru.gosuslugi.pgu.sd.storage.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.file.factory.YamlPropertyLoaderFactory;
import ru.gosuslugi.pgu.sd.storage.ServiceDescriptorClient;
import ru.gosuslugi.pgu.sd.storage.client.ServiceDescriptorClientImpl;

@Configuration
@EnableCaching
@PropertySource(value = "classpath:cache-config.yml", factory = YamlPropertyLoaderFactory.class)
@EnableConfigurationProperties(ServiceDescriptorClientProperties.class)
@AutoConfigureBefore(CacheAutoConfiguration.class)
public class ServiceDescriptorStorageAutoConfiguration {

    @Bean
    public ServiceDescriptorClient serviceDescriptorClient(RestTemplate restTemplate, ServiceDescriptorClientProperties properties) {
        return new ServiceDescriptorClientImpl(restTemplate, properties);
    }
}
