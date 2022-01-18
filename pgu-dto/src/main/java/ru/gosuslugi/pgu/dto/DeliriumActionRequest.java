package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DeliriumActionRequest {
    private ScenarioDto scenarioDto;
    private String deliriumAction;
}
