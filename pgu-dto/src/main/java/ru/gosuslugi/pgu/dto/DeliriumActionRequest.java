package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DeliriumActionRequest {
    @Schema(description = "DTO услуги")
    private ScenarioDto scenarioDto;

    @Schema(description = "Действие для выполнения в delirium")
    private String deliriumAction;
}
