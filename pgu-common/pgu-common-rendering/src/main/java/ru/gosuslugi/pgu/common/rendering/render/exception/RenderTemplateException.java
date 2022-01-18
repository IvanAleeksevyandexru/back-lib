package ru.gosuslugi.pgu.common.rendering.render.exception;

import ru.gosuslugi.pgu.fs.common.exception.FormBaseException;

/**
 * Не удалось обработать шаблон.
 */
public class RenderTemplateException extends FormBaseException {

    public RenderTemplateException(String message) {
        super(message);
    }

    public RenderTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
