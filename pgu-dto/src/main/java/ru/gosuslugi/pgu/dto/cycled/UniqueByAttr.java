package ru.gosuslugi.pgu.dto.cycled;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UniqueByAttr {
    private List<UniqueByKey> keys;
    private UniqueByErrorDisclaimer disclaimer;
}
