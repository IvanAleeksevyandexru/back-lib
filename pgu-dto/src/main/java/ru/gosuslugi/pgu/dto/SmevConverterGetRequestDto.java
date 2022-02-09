package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "Запрос в барбарбок для получения json указанного шаблона для услуги")
public class SmevConverterGetRequestDto {

    @JsonCreator
    public SmevConverterGetRequestDto(@JsonProperty("data") String data,
                                      @JsonProperty("serviceId") String serviceId,
                                      @JsonProperty("templateName") String templateName) {
        this.data = data;
        this.serviceId = serviceId;
        this.templateName = templateName;
    }

    @Schema(description = "Xml-запрос в барбарбок", required = true)
    @NotNull
    private String data;

    @Schema(description = "Код услуги", required = true)
    @NotNull
    private final String serviceId;

    @Schema(description = "Наименование json-шаблона", required = true)
    @NotNull
    private final String templateName;

    @Schema(description = "Дополнительные данные в формате json")
    @JsonProperty("jsonData")
    private String extData;
}