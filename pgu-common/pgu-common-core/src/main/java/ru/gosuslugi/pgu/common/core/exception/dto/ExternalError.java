package ru.gosuslugi.pgu.common.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Информация об ошибке
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ExternalError {
    /** Код ошибки */
    private Integer code;
    /** Сообщение */
    private String message;
}
