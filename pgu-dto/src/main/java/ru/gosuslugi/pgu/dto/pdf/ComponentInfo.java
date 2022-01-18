package ru.gosuslugi.pgu.dto.pdf;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentInfo {

    private String fieldId;
    private String componentType;
    private String screenHeader;

}
