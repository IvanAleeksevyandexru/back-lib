package ru.gosuslugi.pgu.common.esia.search.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gosuslugi.pgu.common.core.exception.PguException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Найдено несколько пользователей с одинаковыми уникально идентифицирующими данными")
public class MultiplePersonFoundException extends PguException {
    public MultiplePersonFoundException(String s) {
        super(s);
    }
}
