package ru.gosuslugi.pgu.dto.descriptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.descriptor.types.ErrorType;

import java.util.List;

/**
 * Описание ошибки компонента
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldComponentError {

    /* Тип */
    private ErrorType type = ErrorType.error;
    /* Заголовок */
    private String title;
    /* Описание */
    private String desc;
    /* Поля с ошибками */
    private List<String> fields;

    public FieldComponentError(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
}
