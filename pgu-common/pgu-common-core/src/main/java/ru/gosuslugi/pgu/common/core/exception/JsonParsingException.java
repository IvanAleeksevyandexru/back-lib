package ru.gosuslugi.pgu.common.core.exception;

/**
 * Ошибка преобразования json в объект
 */
public class JsonParsingException extends PguException {

    public JsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
