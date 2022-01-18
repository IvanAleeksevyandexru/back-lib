package ru.gosuslugi.pgu.dto.descriptor.value.target;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.dto.descriptor.value.service.ServiceId;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TargetIds {
    private String defaultTargetId;
    private List<TargetId> ids;
}
