package ru.gosuslugi.pgu.common.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ошибка валидации запроса
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends PguException {

    public ValidationException(String s) {
        super(s);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
