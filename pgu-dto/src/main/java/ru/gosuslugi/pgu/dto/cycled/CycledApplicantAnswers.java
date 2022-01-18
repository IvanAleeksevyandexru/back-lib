package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CycledApplicantAnswers {
    private String currentAnswerId;
    @JsonProperty("answerlist")
    private List<CycledApplicantAnswer> answers = new LinkedList<>();

    @JsonIgnore
    public CycledApplicantAnswer getCurrentAnswer() {
        return getAnswerOrDefault(currentAnswerId, null);
    }

    @JsonIgnore
    public CycledApplicantAnswer getAnswerOrDefault(String id, CycledApplicantAnswer cycledApplicantAnswer) {
        return answers.stream()
                .filter(answer -> answer.getId().equals(id))
                .findAny()
                .orElse(cycledApplicantAnswer);
    }

    @JsonIgnore
    public Optional<CycledApplicantAnswer> getAnswer(String id) {
        return answers.stream()
                .filter(answer -> answer.getId().equals(id))
                .findAny();
    }

    @JsonIgnore
    public void removeAnswer(String id){
        this.getAnswers().removeAll(this.getAnswers()
                .stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList()));
    }

    @JsonIgnore
    public List<String> getAnswersIds() {
        return answers.stream().map(CycledApplicantAnswer::getId).collect(Collectors.toList());
    }

    @JsonIgnore
    public void addAnswerIfAbsent(String id, CycledApplicantAnswer cycledApplicantAnswer) {
        if (!getAnswersIds().contains(id)) {
            answers.add(cycledApplicantAnswer);
        }
    }
}
