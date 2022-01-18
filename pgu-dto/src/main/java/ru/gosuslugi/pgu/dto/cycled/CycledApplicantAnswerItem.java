package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.dto.ApplicantAnswer;

import java.util.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CycledApplicantAnswerItem {
    private String id;
    private List<String> uniqueKeys = new ArrayList<>();
    private Map<String, String> fieldToId = new HashMap<>();
    private Map<String, ApplicantAnswer> itemAnswers = new HashMap<>();
    private Map<String, ApplicantAnswer> cachedAnswers = new HashMap<>();
    private Map<String, Object> esiaData = new HashMap<>();
    private LinkedList<String> finishedScreens = new LinkedList<>();

    public CycledApplicantAnswerItem(String id) {
        this.id = id;
    }

    public CycledApplicantAnswerItem() {
    }
}
