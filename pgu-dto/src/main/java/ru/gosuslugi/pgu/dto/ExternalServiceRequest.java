package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalServiceRequest {

    @NonNull
    private String serviceId;

    @NonNull
    private String targetId;

    @NonNull
    private String screenId;

    private Map<String, String> answers;
}
