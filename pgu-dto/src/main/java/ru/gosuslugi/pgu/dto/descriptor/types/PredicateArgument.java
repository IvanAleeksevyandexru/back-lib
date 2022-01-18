package ru.gosuslugi.pgu.dto.descriptor.types;

import lombok.Data;

@Data
public class PredicateArgument {
    private String value;
    PredicateArgumentType type;
}
