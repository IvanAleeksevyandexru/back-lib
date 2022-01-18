package ru.gosuslugi.pgu.common.core.exception.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.gosuslugi.pgu.common.core.exception.PguException;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ErrorMessage {

    /**
     * Код ошибки -
     */
    protected String status = HttpStatus.BAD_REQUEST.name();

    /**
     * Текстовое отображение кода ошибки
     */
    protected String message;

    /**
     * Хинт подсказка
     */
    protected String hint;

    /**
     * Описание ошибки
     */
    protected String description;

    /**
     * traceId для поиска лога
     */
    protected String traceId;

    /**
     * В качестве value чаще всего участвует Exception
     */
    protected Object value;

    public ErrorMessage(String status, String message, PguException ex) {
        this.status = status;
        this.message = message;
        this.description = ex.getMessage();
        this.value = ex.getValue();
    }

}
