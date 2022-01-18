package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.util.StringUtils;
import ru.gosuslugi.pgu.dto.descriptor.placeholder.Placeholder;
import ru.gosuslugi.pgu.dto.descriptor.types.ValueDefinition;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedValue {
    private int version = 1;
    private ValueDefinition definition;
    private String argument;
    private Object source;
    private String jsonPath;
    private List<Expression> expressions;
    private String defaultValue;
    private List<Expression> activeOnly;
    private String activeCheckType;
    private CycledAttrs attrs;
    private Object jsonLogic;
    private FunctionDescriptor function;
    private Map<String, Object> converterSettings;

    @JsonIgnore
    private Placeholder reference;

    @JsonIgnore
    private Boolean isJsonSource;

    @JsonIgnore
    private Boolean hasActiveConditions;

    public String evaluateValue(String fieldvalue) {
        if (Objects.isNull(expressions) || expressions.isEmpty()) {
            return fieldvalue;
        }
        Optional<Expression> foundExpressionBox = expressions.stream().filter(expression -> expression.getWhen().equals(fieldvalue)).findAny();
        if (foundExpressionBox.isPresent()) {
            return foundExpressionBox.get().getThen();
        }
        return defaultValue;
    }

    public boolean isJsonSource(){
        if (isJsonSource == null) {
            isJsonSource = false;
            if (reference != null && reference.getPath() != null) {
                isJsonSource = reference.getPath().contains(".");
            }
        }
        return isJsonSource;
    }

    public boolean hasActiveConditions(){
        if (hasActiveConditions == null) {
            hasActiveConditions = Objects.nonNull(activeOnly);
        }
        if (StringUtils.isEmpty(activeCheckType)) {
            activeCheckType = "ANY";
        }
        return hasActiveConditions;
    }

}
