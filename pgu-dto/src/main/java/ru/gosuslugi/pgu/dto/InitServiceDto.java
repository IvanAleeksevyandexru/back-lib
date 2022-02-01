package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Dto that is used to init service (first request to ServiceFrom)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Schema(description = "Дополнительная информация для инициализации услуги")
public class InitServiceDto {

    @Schema(description = "Фактическая цель услуги\n\n" +
            "Например, используется в услуге по уходу за недееспособными")
    private String targetId;

    @Schema(description = "Id заявления находящегося в черновиках. Если указан - загрузится заявление из черновика")
    private String orderId;

    @Schema(description = "DTO с информацией о региональном ведомстве")
    ServiceInfoDto serviceInfo = new ServiceInfoDto();

    @Schema(description = "Идентификатор сообщения ГЭПС. Если указан, то компонент ГЭПС сможет найти нужное сообщение")
    private Long gepsId;
}
