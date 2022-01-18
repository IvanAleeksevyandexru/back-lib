package ru.gosuslugi.pgu.common.core.exception.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModalComponentButtonAction {
    String type;
    String value;
}
