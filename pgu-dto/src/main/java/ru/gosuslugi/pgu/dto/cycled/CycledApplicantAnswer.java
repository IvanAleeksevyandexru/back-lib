package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CycledApplicantAnswer {
    private String id;
    private String initScreen;
    private String currentItemId;
    private List<CycledApplicantAnswerItem> items = new LinkedList<>();

    public CycledApplicantAnswer(String id) {
        this.id = id;
    }

    public CycledApplicantAnswer() {
    }

    @JsonIgnore
    public CycledApplicantAnswerItem getCurrentAnswerItem() {
        return getItemOrDefault(currentItemId, null);
    }

    @JsonIgnore
    public CycledApplicantAnswerItem getItemOrDefault(String id, CycledApplicantAnswerItem cycledApplicantAnswerItem) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findAny()
                .orElse(cycledApplicantAnswerItem);
    }

    @JsonIgnore
    public Optional<CycledApplicantAnswerItem> getItem(String id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findAny();
    }

    @JsonIgnore
    public List<String> getItemsIds() {
        return items.stream().map(CycledApplicantAnswerItem::getId).collect(Collectors.toList());
    }

    @JsonIgnore
    public void addItemIfAbsent(String id, CycledApplicantAnswerItem cycledApplicantAnswerItem) {
        if (!getItemsIds().contains(id)) {
            items.add(cycledApplicantAnswerItem);
        }
    }
}
