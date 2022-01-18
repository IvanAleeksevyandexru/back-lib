package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingInfo {

    private String initScreenId;
    private boolean enabled;
    private List<String> availableStatusList = new ArrayList<>();
    private String bookingLink;
    private Set<RuleCondition> availableIf;

}
