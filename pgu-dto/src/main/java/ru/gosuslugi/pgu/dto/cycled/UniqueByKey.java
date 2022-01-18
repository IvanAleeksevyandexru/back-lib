package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.dto.descriptor.FieldComponentAttrField;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniqueByKey {
    private List<FieldComponentAttrField> fields;
    private String errorMsg;
}
