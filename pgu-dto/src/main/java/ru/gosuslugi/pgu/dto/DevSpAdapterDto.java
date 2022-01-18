package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.LinkedHashMap;

/**
 * Dev mode DTO for SP-Adapter moodule
 * used only for templates dev&debug processes
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class DevSpAdapterDto extends SpAdapterDto {

    /**
     * ScenarioResponse from last step of any service
     */
    private LinkedHashMap<String, Object> scenarioDraftBody;

    public DevSpAdapterDto(String serviceId, Long orderId, Long oid, String role) {
        super(serviceId, orderId, oid, role);
    }
}
