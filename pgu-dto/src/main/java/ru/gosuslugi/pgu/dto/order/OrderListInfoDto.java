package ru.gosuslugi.pgu.dto.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет информацию о заявке
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderListInfoDto {

    /** Является ли заявка "инвайтом" */
    @Schema(description = "Id заявления от которого было создано приглашения")
    private Long inviteByOrderId;

    @Schema(description = "Id заявления которое блокирует заполнение нового заявления")
    private Long startNewBlockedByOrderId;

    @Schema(description = "Лимит заявлений")
    private Long limitOrders;

    @Schema(description = "Информация по заявлениям")
    private List<ShortOrderData> orders = new ArrayList();

    @Schema(description = "Флаг, указывающий нужно ли проверять текущий регион пользователя с регионом в заявлении")
    private Boolean compareRegions = false;

    @Schema(description = "Название услуги из Service Descriptor")
    private String serviceName;

    @Schema(description = "Флаг, указывающий на то, что услуга закрыта кукой")
    private Boolean closedByCookie = false;
}
