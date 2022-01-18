package ru.gosuslugi.pgu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TimeSlotRequestAttr {
    private String name;
    private String value;
}
