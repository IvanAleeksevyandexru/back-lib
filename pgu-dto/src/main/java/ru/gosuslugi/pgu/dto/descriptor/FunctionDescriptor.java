package ru.gosuslugi.pgu.dto.descriptor;

import lombok.Data;

import java.util.List;

@Data
public class FunctionDescriptor {
    private String name;
    private List<String> args;
}
