package ru.gosuslugi.pgu.sp.adapter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Nonnull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "sp.integration")
public class SpAdapterProperties {

    @Nonnull
    private String url;

    private String mockFile;

    private boolean enabled;
}