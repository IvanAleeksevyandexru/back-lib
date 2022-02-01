package ru.gosuslugi.pgu.common.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthDto {

    /**
     * Словари
     */
    @Schema(description = "Словари")
    private List<DictionayHealthDto> dictionaries = new ArrayList<>();
}
