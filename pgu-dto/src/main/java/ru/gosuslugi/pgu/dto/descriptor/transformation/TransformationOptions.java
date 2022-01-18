package ru.gosuslugi.pgu.dto.descriptor.transformation;

import lombok.Data;

@Data
public class TransformationOptions {

    /**
     * Не добавлять запись о трансформации в историю
     */
    private boolean skipHistory = false;

}
