package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.descriptor.types.ConditionFieldType;
import ru.gosuslugi.pgu.dto.descriptor.types.PredicateArgument;

import java.util.List;

/**
 * Class representing condition for service rule
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleCondition {
    private ConditionFieldType fieldType;
    private String predicate;
    private List<PredicateArgument> args;
    //dot separated attribute reference path
    private String field;

    private String protectedField;
    private String variable;

    private Boolean visited;
    private String value;

    @JsonIgnore
    public boolean hasPredicate() {
        return this.predicate != null;
    }

    @JsonIgnore
    public boolean isConditionWithProtectedField() {
        return this.protectedField != null;
    }

    @JsonIgnore
    public boolean isConditionWithVariable() {
        return this.variable != null;
    }
}
