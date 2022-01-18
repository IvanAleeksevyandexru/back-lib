package ru.gosuslugi.pgu.dto.csv;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Результат парсинга
 */
@Data
public class CsvParseResult {
    private Boolean isSuccess = true;
    private String error;
    private List<Map<String, String>> data;

    public CsvParseResult(Boolean isSuccess, String error) {
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public CsvParseResult(List<Map<String, String>> data) {
        this.data = data;
    }
}
