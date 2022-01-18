package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioGeneratorDto {
    private String serviceId;
    private String routeNumber;
    private String billNumber;
    private Date billDate;
    private String token;
}
