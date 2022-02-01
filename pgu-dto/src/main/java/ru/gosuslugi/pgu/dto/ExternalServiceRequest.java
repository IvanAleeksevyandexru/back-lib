package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalServiceRequest {

    @NonNull
    @Schema(description = "Id услуги")
    private String serviceId;

    @NonNull
    @Schema(description = "Фактическая цель услуги\n\n" +
            "Например, используется в услуге по уходу за недееспособными")
    private String targetId;

    @NonNull
    @Schema(description = "Id начального экрана")
    private String screenId;

    @Schema(description = "Ответы пользователя для инициализации услуги")
    private Map<String, String> answers;
}
