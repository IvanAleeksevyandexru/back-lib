package ru.gosuslugi.pgu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Результаты квиза")
public class QuizRequest {

    @ApiModelProperty(notes = "DTO сценария услуги")
    private ScenarioDto scenarioDto;

    @ApiModelProperty(notes = "Id услуги для перехода")
    private String serviceId;

    @ApiModelProperty(notes = "Id цели услуги для перехода")
    private String targetId;
}
