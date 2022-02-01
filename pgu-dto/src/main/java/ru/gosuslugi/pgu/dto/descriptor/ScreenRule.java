package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ScreenRule {

    /**
     * Conditions to be met (in case of several behaves as &&)
     */
    private LinkedHashSet<RuleCondition> conditions = new LinkedHashSet<>();

    /**
     * Screen id to display
     */
    private String nextDisplay;
}
