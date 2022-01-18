package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * DTO с дополнительной информацией для услуги
 * https://jira.egovdev.ru/browse/EPGUCORE-46548
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "DTO с дополнительной информацией для услуги")
public class ServiceInfoDto {

    /** Информация о ведомстве */
    @ApiModelProperty(notes = "Информация о ведомстве")
    private StateOrgDto department;

    /** Код маршрутизации */
    @ApiModelProperty(notes = "Код маршрутизации")
    private String routingCode;

    /** Код региональной ИС */
    @ApiModelProperty(notes = "Код региональной ИС")
    private Boolean formPrefilling;

    /** Флаг "Регион предоставляет данные для предзаполнения формы" */
    @ApiModelProperty(notes = "Флаг \"Регион предоставляет данные для предзаполнения формы\"")
    private String infSysCode;

    /** Информация об ошибке */
    @ApiModelProperty(notes = "Информация об ошибке")
    private String error;

    /** Регион пользователя */
    @ApiModelProperty(notes = "Регион пользователя")
    private UserRegionDto userRegion;

    /** Стутус код (из ЛК) */
    @ApiModelProperty(notes = "Стутус код (из ЛК)")
    private Long statusId;

    private String routeNumber;
    private String billNumber;
    private String orderType;

    /**
     * Параметры из URL
     * https://jira.egovdev.ru/browse/EPGUCORE-57156
     */
    @ApiModelProperty(notes = "Параметры из URL")
    private Map<String, String> queryParams;

    private String formId;
}
