package ru.gosuslugi.pgu.dto.descriptor;

import lombok.Data;

@Data
public class Expression {
    private String when;
    private String then;
    private String field;
}
