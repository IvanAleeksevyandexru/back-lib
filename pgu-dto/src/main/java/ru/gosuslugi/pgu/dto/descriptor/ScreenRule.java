package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ScreenRule {

    /**
     * Conditions to be met (in case of several behaves as &&)
     */
    private Set<RuleCondition> conditions = new HashSet<>();

    /**
     * Screen id to display
     */
    private String nextDisplay;
}
