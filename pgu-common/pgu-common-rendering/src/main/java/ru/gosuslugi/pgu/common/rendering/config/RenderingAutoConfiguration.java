package ru.gosuslugi.pgu.common.rendering.config;

import ru.gosuslugi.pgu.common.core.file.factory.YamlPropertyLoaderFactory;
import ru.gosuslugi.pgu.common.rendering.template.config.props.TemplateServiceProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.gosuslugi.pgu.common.rendering")
@PropertySource(value = "classpath:rendering.yml", factory = YamlPropertyLoaderFactory.class)
@EnableConfigurationProperties(TemplateServiceProperties.class)
public class RenderingAutoConfiguration {
}
