package ru.gosuslugi.pgu.dto.pdf;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenderTemplateResponse {
    String errorInfo;
    String resultData;

    public String toString() {
        return "<status>" + errorInfo + "</status>" + resultData;
    }
}
