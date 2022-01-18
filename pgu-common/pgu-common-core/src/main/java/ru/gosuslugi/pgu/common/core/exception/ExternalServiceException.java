package ru.gosuslugi.pgu.common.core.exception;


import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessage;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Ошибка взаимодействия с внешней системой
 */
@Getter
public class ExternalServiceException extends PguException {

    private ErrorMessage errorBody;
    private HttpStatus status;

    public ExternalServiceException(String s) {
        super(s);
    }

    public ExternalServiceException(ErrorMessage errorBody, String s) {
        super(s);
        this.errorBody = errorBody;
    }
    public ExternalServiceException(ErrorMessage errorBody, String s, HttpStatus status) {
        super(s);
        this.errorBody = errorBody;
        this.status = status;
    }

    public ExternalServiceException(Throwable cause) {
        super("Error on communication with external system: " + cause.getMessage(), cause);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalServiceException(String s, HttpStatus status) {
        super(s);
        this.status = status;
    }

    public ExternalServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    @Override
    public Object getValue() {
        return errorBody != null ? errorBody.getValue() : null;
    }

}
