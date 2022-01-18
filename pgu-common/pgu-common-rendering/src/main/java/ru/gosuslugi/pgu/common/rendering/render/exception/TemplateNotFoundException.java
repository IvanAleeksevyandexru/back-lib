package ru.gosuslugi.pgu.common.rendering.render.exception;

import ru.gosuslugi.pgu.fs.common.exception.FormBaseException;

/**
 * Не удалось найти шаблон.
 */
public class TemplateNotFoundException extends FormBaseException {

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
