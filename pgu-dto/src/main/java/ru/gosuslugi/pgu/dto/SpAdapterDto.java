package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpAdapterDto {
    private String serviceId;
    private String targetId;
    private Long orderId;
    private Long oid;
    private String role;
    private String requestGuid;
    private Long orgId;
    private boolean isSigned = false;

    public SpAdapterDto(String serviceId, Long orderId, Long oid, String role) {
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.oid = oid;
        this.role = role;
    }

    public SpAdapterDto(String serviceId, Long orderId, Long oid, String role, String targetTopic, boolean isSigned) {
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.oid = oid;
        this.role = role;
        this.isSigned = isSigned;
    }
}
