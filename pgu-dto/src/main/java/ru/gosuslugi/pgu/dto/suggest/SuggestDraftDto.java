package ru.gosuslugi.pgu.dto.suggest;

import lombok.Data;
import ru.gosuslugi.pgu.dto.ScenarioDto;

@Data
public class SuggestDraftDto {

    private Long userId;

    private ScenarioDto scenarioDto;
}
