package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Request dto
 * contains information about selected values, user token etc
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(description = "Информация для перехода по сценарию")
public class ScenarioRequest{

    @ApiModelProperty(notes = "DTO сценария услуги")
    @Schema(description = "DTO сценария услуги")
    private ScenarioDto scenarioDto;

    @ApiModelProperty(notes = "Id заявления, на которое нужно перейти при выходе из подсценария")
    @Schema(description = "Id заявления, на которое нужно перейти при выходе из подсценария")
    private Long callBackOrderId;

    @ApiModelProperty(notes = "Id услуги, на которую нужно перейти при выходе из подсценария")
    @Schema(description = "Id услуги, на которую нужно перейти при выходе из подсценария")
    private String callBackServiceId;

    @ApiModelProperty(notes = "Флаг указывающий на то находимся мы в подсценарии или нет")
    @Schema(description = "Флаг указывающий на то находимся мы в подсценарии или нет")
    private Boolean isInternalScenario;

}
