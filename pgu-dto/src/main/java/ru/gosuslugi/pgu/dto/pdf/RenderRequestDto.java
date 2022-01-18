package ru.gosuslugi.pgu.dto.pdf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class RenderRequestDto {
    private Long orderId;
    private Long oid;
    private String serviceId;
    private String templateFileName;
    private Map<String, Object> values;
    private Boolean required;
    private boolean allowDefaultGeneration;

    private DescriptorStructure descriptorStructure;
}
