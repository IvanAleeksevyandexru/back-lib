package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniqueByErrorDisclaimer {
    private String title;
    private String description;
    private Map<String, Object> clarifications;
    private Set<String> uniquenessErrors;
    private String type;
}
