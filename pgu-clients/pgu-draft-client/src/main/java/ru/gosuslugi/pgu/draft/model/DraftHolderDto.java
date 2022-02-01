package ru.gosuslugi.pgu.draft.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DraftHolderDto {

    @Schema(description = "Id заявления")
    private long orderId;

    @Schema(description = "Id пользователя для которого был создан данный черновик")
    private Long oid;

    @Schema(description = "Id организации для которой был создан данный черновик")
    private Long orgId;

    @Schema(description = "DTO услуги")
    private ScenarioDto body;

    @Schema(description = "Id услуги")
    private String type;

    @Schema(description = "Флаг блокировки черновика")
    private boolean locked;

    @Schema(description = "Время обновления черновика")
    private OffsetDateTime updated;

    @Schema(description = "Время жизни черновика в секундах")
    private Integer ttlInSec;

    @Schema(description = "Время жизни заявления в секундах")
    private Integer orderTtlInSec;
}
