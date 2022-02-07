package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Результат ответа BackRestCallService
 */
@Getter
@AllArgsConstructor
public class BackRestCallResponseDto {

    private int statusCode;
    @JsonInclude()
    private String errorMessage;
    private Object response;

    public BackRestCallResponseDto(int statusCode, Object response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
