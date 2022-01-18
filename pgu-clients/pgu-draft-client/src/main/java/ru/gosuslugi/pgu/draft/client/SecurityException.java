package ru.gosuslugi.pgu.draft.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SecurityException extends RuntimeException{
    public SecurityException(String message) {
        super(message);
    }
}
