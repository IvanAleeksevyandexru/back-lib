package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Запрос на конвертацию данных из xml и других источников в произвольный шаблон")
public class XmlCustomConvertRequest {

    @JsonCreator
    public XmlCustomConvertRequest(@JsonProperty("xmlData") String xmlData,
                                   @JsonProperty("serviceId") String serviceId,
                                   @JsonProperty("templateName") String templateName,
                                   @JsonProperty("jsonData") String jsonData) {
        this.xmlData = xmlData;
        this.serviceId = serviceId;
        this.templateName = templateName;
        this.jsonData = jsonData;
    }

    @Schema(description = "Xml-данные, например, из барбарбока", required = true)
    @NotNull
    private final String xmlData;

    @Schema(description = "Код услуги", required = true)
    @NotNull
    private String serviceId;

    @Schema(description = "Наименование json-шаблона", required = true)
    @NotNull
    private String templateName;

    @Schema(description = "Дополнительные данные в формате json")
    private String jsonData;
}
