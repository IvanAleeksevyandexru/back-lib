package ru.gosuslugi.pgu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Результаты квиза")
public class QuizRequest {

    @Schema(description = "DTO сценария услуги")
    private ScenarioDto scenarioDto;

    @Schema(description = "Id услуги для перехода")
    private String serviceId;

    @Schema(description = "Id цели услуги для перехода")
    private String targetId;
}
