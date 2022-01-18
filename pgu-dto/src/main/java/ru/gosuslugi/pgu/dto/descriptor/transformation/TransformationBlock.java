package ru.gosuslugi.pgu.dto.descriptor.transformation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformationBlock {

    private String statusId;

    private TransformationOptions options = new TransformationOptions();

    private List<TransformationRule> rules = new ArrayList<>();
}
