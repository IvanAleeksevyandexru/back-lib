package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Dto that is used to init service (first request to ServiceFrom)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(description = "Дополнительная информация для инициализации услуги")
public class InitServiceDto {
    @ApiModelProperty(notes = "Id цели услуги")
    private String targetId;

    @ApiModelProperty(notes = "Id заявления находящегося в черновиках. Если указан - загрузится заявление из черновика", allowEmptyValue = true)
    private String orderId;

    @ApiModelProperty(notes = "DTO с информацией о региональном ведомстве")
    ServiceInfoDto serviceInfo = new ServiceInfoDto();

    @ApiModelProperty(notes = "Идентификатор сообщения ГЭПС. Если указан, то компонент ГЭПС сможет найти нужное сообщение", allowEmptyValue = true)
    private Long gepsId;
}
