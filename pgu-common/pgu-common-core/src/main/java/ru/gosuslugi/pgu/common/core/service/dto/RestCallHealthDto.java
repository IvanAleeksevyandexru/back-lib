package ru.gosuslugi.pgu.common.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class RestCallHealthDto extends DictionayHealthDto {

    /** Дополнительные агументы. */
    @Schema(description = "Дополнительные агументы")
    private final Map<String, String> args;

    public RestCallHealthDto(String id, String url, HttpMethod method, HttpStatus status, String error, String errorMessage, Map<String, String> args) {
        super(id, url, method, status, error, errorMessage, null);
        this.args = args;
    }
}
