package ru.gosuslugi.pgu.common.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessage;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends PguQuietException {

    private ErrorMessage errorBody;

    public EntityNotFoundException(String s) {
        super(s);
    }

    public EntityNotFoundException(ErrorMessage errorBody, String s) {
        super(s);
        this.errorBody = errorBody;
    }

    @Override
    public Object getValue() {
        return errorBody.getValue();
    }
}
