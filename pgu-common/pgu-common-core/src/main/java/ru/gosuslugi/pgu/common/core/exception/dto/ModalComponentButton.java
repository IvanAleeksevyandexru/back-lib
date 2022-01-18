package ru.gosuslugi.pgu.common.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModalComponentButton {
    private String label;
    private String color;
    private String value;
    private boolean closeModal;
    private ModalComponentButtonAction action;
}
