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
    @JsonProperty(value = "eServiceFullName")
    private String eServiceFullName;
    @JsonProperty(value = "eServiceStateStructureId")
    private String eServiceStateStructureId;
    @JsonProperty(value = "eServiceStateStructureName")
    private String eServiceStateStructureName;
    @JsonProperty(value = "eServicePassportExtId")
    private String eServicePassportExtId;
    @JsonProperty(value = "eServiceAttrEpguCode")
    private String eServiceAttrEpguCode;
    @JsonProperty(value = "eserviceAttrServiceNameForOrder")
    private String eserviceAttrServiceNameForOrder;
    private Boolean eserviceAttrDeleteDraft = false;

}
