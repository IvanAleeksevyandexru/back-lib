package ru.gosuslugi.pgu.dto.descriptor.types;

import lombok.Data;

import java.util.Map;

@Data
public class SubHeader {
    String text;
    Map<String, Object> clarifications;
}
