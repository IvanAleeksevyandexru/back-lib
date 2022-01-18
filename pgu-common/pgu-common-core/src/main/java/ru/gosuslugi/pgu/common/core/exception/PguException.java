package ru.gosuslugi.pgu.common.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PguException extends RuntimeException {
    public PguException(String message) {
        super(message);
    }

    public PguException(String message, Throwable cause) {
        super(message, cause);
    }

    public PguException(Throwable cause) {
        super(cause);
    }

    /**
     * @return null по дефаулту или мета информация об ошибке
     */
    public Object getValue() {
        return null;
    }

}
