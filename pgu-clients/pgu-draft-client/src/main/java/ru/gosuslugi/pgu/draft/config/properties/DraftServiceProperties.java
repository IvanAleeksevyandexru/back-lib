package ru.gosuslugi.pgu.draft.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nonnull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "draft-client")
public class DraftServiceProperties {

    private boolean enabled = true;

    private String mockFile;

    @Nonnull
    private String url;

}
