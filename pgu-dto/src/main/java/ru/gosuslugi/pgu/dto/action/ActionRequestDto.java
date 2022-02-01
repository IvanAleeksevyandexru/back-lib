package ru.gosuslugi.pgu.dto.action;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.util.HashMap;
import java.util.Map;

@Data
public class ActionRequestDto {

    @Schema(description = "DTO услуги")
    ScenarioDto scenarioDto;

    @Schema(description = "Дополнительные параметры")
    Map<String, Object> additionalParams = new HashMap<>();

}
