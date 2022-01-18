package ru.gosuslugi.pgu.draft.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DraftHolderDto {

    private long orderId;

    private Long oid;

    private Long orgId;

    private ScenarioDto body;

    private String type;

    private boolean locked;

    private OffsetDateTime updated;

    private Integer ttlInSec;

    private Integer orderTtlInSec;
}
