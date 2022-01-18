package ru.gosuslugi.pgu.common.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class DictionayHealthDto {

    /** Идентификатор ресурса. */
    private final String id;

    /** URL услуги */
    private final String url;

    /** HTTP Метод */
    private final HttpMethod method;

    /** HTTP код */
    private final HttpStatus status;

    /** Оригинальная ошибка */
    private final String error;

    /** Сообщение */
    private final String errorMessage;

    /** Окато */
    private final String okato;
}
