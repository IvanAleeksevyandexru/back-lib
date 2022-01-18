package ru.gosuslugi.pgu.dto.pdf;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

// TODO KK Перенести этот класс в модуль pdf-generator-ms после того, как генерация pdf будет выпилена из sp-adapter
@Data
public class DescriptorStructure {

    private String service;

    /**
     * Поля услуги из ServiceDescriptor на названия компонентов: a1 -> In
     */
    LinkedHashMap<String, ComponentInfo> fieldToComponentType;


}
