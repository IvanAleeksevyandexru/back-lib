package ru.gosuslugi.pgu.common.rendering.render.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Опции для конфигурации {@link org.apache.velocity.app.VelocityEngine}.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "velocity")
public class VelocityProperties {
    private ResourceLoader resourceLoader;
    private String resourceLoaderFileCache;
    private String resourceLoaderFileModificationCheckInterval;

    private String fileResourceLoaderClass = "org.apache.velocity.runtime.resource.loader.FileResourceLoader";
    private String classResourceLoaderClass = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

    public enum ResourceLoader {
        /**
         * Управляет загрузкой ресурсов из файловой системы.
         */
        FILE,
        /**
         * Управляет загрузкой ресурсов из classpath.
         */
        CLASS
    }
}
