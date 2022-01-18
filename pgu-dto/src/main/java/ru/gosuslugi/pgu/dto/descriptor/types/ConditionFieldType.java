package ru.gosuslugi.pgu.dto.descriptor.types;

import lombok.Getter;

public enum ConditionFieldType {
    String(false),
    Integer(false),
    Boolean(false),
    Date(false),
    Array(true),
    ApplicantAnswer(true);

    ConditionFieldType(boolean isAppliesToNullValue) {
        this.isAppliesToNullValue = isAppliesToNullValue;
    }

    @Getter
    private final boolean isAppliesToNullValue;

}
