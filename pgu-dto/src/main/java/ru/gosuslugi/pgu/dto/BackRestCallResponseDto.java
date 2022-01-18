package ru.gosuslugi.pgu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Результат ответа BackRestCallService
 */
@Getter
@AllArgsConstructor
public class BackRestCallResponseDto {

    private int statusCode;
    private String errorMessage;
    private Object response;

    public BackRestCallResponseDto(int statusCode, Object response) {
        this.statusCode = statusCode;
        this.response = response;
    }
}
