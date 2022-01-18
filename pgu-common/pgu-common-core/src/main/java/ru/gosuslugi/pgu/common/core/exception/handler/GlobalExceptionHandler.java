package ru.gosuslugi.pgu.common.core.exception.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessage;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessageWithoutModal;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionHandlerHelper exceptionHandlerHelper;

    @ExceptionHandler(PguException.class)
    public ResponseEntity<? extends ErrorMessage> handleException(PguException ex) {
        return exceptionHandlerHelper.handle(ex, (reason, status) -> new ErrorMessageWithoutModal(reason, status, ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<? extends ErrorMessage> handleException(Exception ex) {
        return handleException(new PguException("Unexpected server error: " + ex.getMessage(), ex));
    }

}
