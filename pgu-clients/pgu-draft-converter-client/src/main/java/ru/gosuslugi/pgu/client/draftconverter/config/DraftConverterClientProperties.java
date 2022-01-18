package ru.gosuslugi.pgu.client.draftconverter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "draft-converter.integration")
public class DraftConverterClientProperties {

    private Boolean enabled;
    private String url;
}
