package ru.gosuslugi.pgu.dto.descriptor.transformation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties
public class TransformationRule {

    /**
     * Operation
     */
    private TransformationOperation operation;

    /**
     * Specification
     */
    private Map<String, Object> spec;
}
