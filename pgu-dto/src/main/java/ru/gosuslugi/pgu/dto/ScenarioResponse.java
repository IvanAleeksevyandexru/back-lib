package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.common.core.service.dto.HealthDto;

/**
 * Request dto
 * contains information about selected values, user token etc
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ScenarioResponse{

    public static final ScenarioResponse EMPTY_SCENARIO = null;

    private ScenarioDto scenarioDto;
    private Boolean isInviteScenario = false;
    private String errorMessage;
    private Long callBackOrderId;
    private String callBackServiceId;
    private Boolean isInternalScenario;
    private Boolean canStartNew = false;

    private HealthDto health;
}
