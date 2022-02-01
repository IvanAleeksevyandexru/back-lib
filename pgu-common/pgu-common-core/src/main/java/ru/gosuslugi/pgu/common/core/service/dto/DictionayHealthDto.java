package ru.gosuslugi.pgu.common.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class DictionayHealthDto {

    /** Идентификатор ресурса. */
    @Schema(description = "Идентификатор ресурса")
    private final String id;

    /** URL услуги */
    @Schema(description = "URL услуги")
    private final String url;

    /** HTTP Метод */
    @Schema(description = "HTTP Метод")
    private final HttpMethod method;

    /** HTTP код */
    @Schema(description = "HTTP код")
    private final HttpStatus status;

    /** Оригинальная ошибка */
    @Schema(description = "Оригинальная ошибка")
    private final String error;

    /** Сообщение */
    @Schema(description = "Сообщение")
    private final String errorMessage;

    /** Окато */
    @Schema(description = "Окато")
    private final String okato;
}
