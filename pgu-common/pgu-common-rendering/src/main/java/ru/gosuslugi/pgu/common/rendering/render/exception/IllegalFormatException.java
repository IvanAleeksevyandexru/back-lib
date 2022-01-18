package ru.gosuslugi.pgu.common.rendering.render.exception;

import ru.gosuslugi.pgu.fs.common.exception.FormBaseException;

/**
 * Неверный формат данных.
 */
public class IllegalFormatException extends FormBaseException {

    public IllegalFormatException(String message) {
        super(message);
    }

    public IllegalFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFormatException(Throwable cause) {
        super(cause);
    }
}
