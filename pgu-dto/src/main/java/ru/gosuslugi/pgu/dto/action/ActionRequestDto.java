package ru.gosuslugi.pgu.dto.action;

import lombok.Data;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.util.HashMap;
import java.util.Map;

@Data
public class ActionRequestDto {

    ScenarioDto scenarioDto;

    Map<String, Object> additionalParams = new HashMap<>();
}
