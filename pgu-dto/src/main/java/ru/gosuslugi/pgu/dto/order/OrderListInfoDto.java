package ru.gosuslugi.pgu.dto.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long inviteByOrderId;

    private Long startNewBlockedByOrderId;

    private Long limitOrders;

    private List<ShortOrderData> orders = new ArrayList();




}
