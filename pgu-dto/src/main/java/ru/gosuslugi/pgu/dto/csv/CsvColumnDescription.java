package ru.gosuslugi.pgu.dto.csv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Описание csv-колонки
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CsvColumnDescription {
    /** Наименование */
    private String name;
    /** Обязательность */
    private boolean required;
    /** Маска для регулярного выражения */
    private String mask;
    /** Уникальность значения */
    private boolean unique;
    /** Сообщение об ошибке выдаваемое при не соответствии маске */
    private String errorMsg;
}
