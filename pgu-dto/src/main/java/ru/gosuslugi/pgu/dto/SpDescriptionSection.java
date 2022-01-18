package ru.gosuslugi.pgu.dto;

import lombok.Data;
import ru.gosuslugi.pgu.dto.descriptor.FieldComponent;
import ru.gosuslugi.pgu.dto.descriptor.ScreenDescriptor;

import java.util.List;
import java.util.Map;

/**
 * Класс реализует секцию JSON  в описании услуги
 * @see Descriptor
 */
@Data
public class SpDescriptionSection {

    private List<ScreenDescriptor> screens;

    private List<FieldComponent> applicationFields;

    private String service;

    private Descriptor spConfig;

    private Map<String, String> parameters;
}
