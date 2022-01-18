package ru.gosuslugi.pgu.common.versioning.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
public class RequestEnvironment {
    @Value("${spring.application.env}")
    private String env;
}
