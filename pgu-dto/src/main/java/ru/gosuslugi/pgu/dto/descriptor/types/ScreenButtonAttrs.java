package ru.gosuslugi.pgu.dto.descriptor.types;

import lombok.Data;

import java.util.List;

@Data
public class ScreenButtonAttrs {
    private Integer stepsBack;
    private List<OperationalSystemType> showOnOS;
    private Boolean hidden;
    private List<Object> ref;
}
