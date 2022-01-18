package ru.gosuslugi.pgu.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ScenarioFromExternal {

    private String screenId;
    private String targetId;
    private String serviceId;
    private Map<String,String> externalApplicantAnswers;
}
