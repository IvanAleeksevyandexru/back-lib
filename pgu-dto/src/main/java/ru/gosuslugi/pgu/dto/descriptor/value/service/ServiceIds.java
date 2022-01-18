package ru.gosuslugi.pgu.dto.descriptor.value.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ServiceIds {
    private String defaultServiceId;
    private List<ServiceId> ids;
}
