package ru.gosuslugi.pgu.dto.csv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Описание проверок при парсинге CSV файла
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CsvParseDescription {
    /** Кодировка файла */
    private String charset;
    /** Разделитель между данными */
    private String delimiter;
    /** Максимально допустимое кол-во строк */
    private Integer maxRowsCount;
    /** Описание колонок */
    private List<CsvColumnDescription> columnDescriptions;
}
