package ru.gosuslugi.pgu.dto.descriptor.types;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenButton {
    private String id;
    private String label;
    private String value;
    private String type;
    private String action;
    private String color;
    private String deliriumAction;
    private ScreenButtonAttrs attrs;
    private List<ScreenButtonMultipleAnswer> multipleAnswers;
    private Map<String, String> queryParams;

    @JsonAnySetter
    public void putAttributeValue(String key, String value) {
        if (queryParams == null) {
            queryParams = new HashMap<>();
        }
        queryParams.put(key, value);
    }
}
