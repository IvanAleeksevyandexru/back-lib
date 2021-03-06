package ru.gosuslugi.pgu.dto.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
@NoArgsConstructor
@AllArgsConstructor
public class HighloadParameters {

    private boolean enabled = true;

    private String sourceSystem;

    private String location;

    private String eServiceCode;

    private String serviceTargetCode;

    private String orderType = "ORDER";

    private Long orderStatusId = 17L;
    @JsonProperty(value = "eserviceFullName")
    private String eserviceFullName;
    @JsonProperty(value = "eserviceStateStructureId")
    private String eserviceStateStructureId;
    @JsonProperty(value = "eserviceStateStructureName")
    private String eserviceStateStructureName;
    @JsonProperty(value = "eservicePassportExtId")
    private String eservicePassportExtId;
    @JsonProperty(value = "eserviceAttrEpguCode")
    private String eserviceAttrEpguCode;
    @JsonProperty(value = "eserviceAttrServiceNameForOrder")
    private String eserviceAttrServiceNameForOrder;
    private Boolean eserviceAttrDeleteDraft = false;
    @JsonProperty(value = "eserviceAttrPassCode")
    private String eserviceAttrPassCode;
@JsonProperty(value = "eserviceStateOrgCode")
    private String eserviceStateOrgCode;
}
