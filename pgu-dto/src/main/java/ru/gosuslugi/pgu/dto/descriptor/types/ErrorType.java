package ru.gosuslugi.pgu.dto.descriptor.types;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ErrorType {
    error,
    warn,
    info,
    ;

    private static final Map<String, ErrorType> errorTypes =
            Stream.of(values()).collect(Collectors.toMap(Enum::name, Function.identity()));

    public static ErrorType fromStringOrDefault(String dictType, ErrorType defaultType) {
        return errorTypes.getOrDefault(dictType, defaultType);
    }
}
