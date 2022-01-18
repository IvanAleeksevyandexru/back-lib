package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Запрос на конвертацию данных из СМЭВ XML и других источников в данные черновика или nsi справочника
 */
@Data
@Schema(description = "Запрос на конвертацию данных из XML и других источников в поля черновика или nsi справочника")
public class XmlDraftConvertRequest {

    @JsonCreator
    public XmlDraftConvertRequest(@JsonProperty("xmlData") String xmlData) {
        this.xmlData = xmlData;
    }

    /**
     * XML-данные.
     */
    @Schema(description = "Входные данные в формате XML, UTF-8", required = true)
    @NotNull
    private final String xmlData;
    /**
     * Вспомогательная информация в JSON.
     */
    @Schema(description = "Вспомогательная информация в формате JSON, UTF-8")
    private String jsonData;
    /**
     * Код услуги.
     */
    @JsonIgnore
    private String serviceId;
}
