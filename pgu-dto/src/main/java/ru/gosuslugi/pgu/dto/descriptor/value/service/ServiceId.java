package ru.gosuslugi.pgu.dto.descriptor.value.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.dto.descriptor.RuleCondition;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ServiceId {
    private String id;
    private Set<RuleCondition> conditions = new HashSet<>();
}
