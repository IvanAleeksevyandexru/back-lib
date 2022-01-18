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
public class OrderInfoDto {
    /** Является ли заявка "инвайтом" */
    private Boolean isInviteScenario = false;

    /** Может ли пользователь начать заполнение заявки заново */
    private Boolean canStartNew = false;


}
