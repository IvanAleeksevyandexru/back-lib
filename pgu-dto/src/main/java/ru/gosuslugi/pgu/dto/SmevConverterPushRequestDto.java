package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "Запрос в барбарбок")
public class SmevConverterPushRequestDto {

    @JsonCreator
    public SmevConverterPushRequestDto(@JsonProperty("data") String data,
                                       @JsonProperty("serviceId") String serviceId) {
        this.data = data;
        this.serviceId = serviceId;
    }

    @Schema(description = "Xml-запрос в барбарбок", required = true)
    @NotNull
    private String data;

    @Schema(description = "Код услуги", required = true)
    @NotNull
    private final String serviceId;
}