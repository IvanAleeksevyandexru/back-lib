package ru.gosuslugi.pgu.common.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModalWindowButton {
    private String componentId;
    private String label;
    private String value;
    private String type;
    private String action;
    private String color;
    private String deliriumAction;
}
