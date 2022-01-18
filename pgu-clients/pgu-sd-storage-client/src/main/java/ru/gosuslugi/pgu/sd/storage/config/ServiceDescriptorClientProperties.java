package ru.gosuslugi.pgu.sd.storage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "service-descriptor-storage-client")
public class ServiceDescriptorClientProperties {

    @Nonnull
    private String url;
}
