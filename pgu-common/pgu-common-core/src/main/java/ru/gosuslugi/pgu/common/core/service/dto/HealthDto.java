package ru.gosuslugi.pgu.common.core.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthDto {

    /**
     * Словари
     */
    private List<DictionayHealthDto> dictionaries = new ArrayList<>();
}
