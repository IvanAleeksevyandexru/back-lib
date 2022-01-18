package ru.gosuslugi.pgu.dto;

import lombok.Data;
import ru.gosuslugi.pgu.dto.catalog.CatalogInfoDtoList;

@Data
public class QuizResponse {

    private ScenarioDto scenarioDto;

    private CatalogInfoDtoList catalogInfo;
}
