package ru.gosuslugi.pgu.common.core.exception.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpMethod;

/**
 * DTO Ошибка обращения к внешнему сервису
 */
@Getter
@Builder
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalErrorInfo {

    /** Идентификатор ресурса. Пример: dictionaryName равное "PGS_CNSTR_ORG" */
    private final String id;

    /** URL услуги */
    private final String url;

    /** HTTP Метод */
    private final HttpMethod method;

    /** Сообщение */
    private final String message;

    /** Окато */
    private final String okato;
}
