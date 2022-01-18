package ru.gosuslugi.pgu.common.rendering.template.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = TemplateServiceProperties.PREFIX)
public class TemplateServiceProperties {
    public static final String PREFIX = "template-service";
    private String fileResourceLoaderPath;
}
