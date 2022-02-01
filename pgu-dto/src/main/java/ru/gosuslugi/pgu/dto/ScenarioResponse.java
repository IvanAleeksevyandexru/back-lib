package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "DTO услуги")
    private ScenarioDto scenarioDto;

    @Schema(description = "Сценарий выполняется по приглашению (заявителя или со-заявителя)")
    private Boolean isInviteScenario = false;

    @Schema(description = "Описание ошибки")
    private String errorMessage;

    @Schema(description = "Id заявления, на которое нужно перейти при выходе из подсценария")
    private Long callBackOrderId;

    @Schema(description = "Id услуги, на которую нужно перейти при выходе из подсценария")
    private String callBackServiceId;

    @Schema(description = "Флаг указывающий на то находимся мы в подсценарии или нет")
    private Boolean isInternalScenario;

    @Schema(description = "Возможность перезапуска сценария")
    private Boolean canStartNew = false;

    @Schema(description = "Информация о работоспособности справочников")
    private HealthDto health;

}
