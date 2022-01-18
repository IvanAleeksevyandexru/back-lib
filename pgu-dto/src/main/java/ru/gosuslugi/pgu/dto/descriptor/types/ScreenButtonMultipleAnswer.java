package ru.gosuslugi.pgu.dto.descriptor.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Дополнительный объект для ScreenButton для поддержки работы умного поиска
 * В нем содержится информация о дальнейших экранах (объеденная информация о экране и правилах перехода)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenButtonMultipleAnswer {
    String screenId;
    String componentId;
    Integer priority;
    Object value;
}
