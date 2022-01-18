package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Информацию о ведомстве
 * https://jira.egovdev.ru/browse/EPGUCORE-46548
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StateOrgDto {

    /** Идентификатор */
    private String id;

    /** Наименование */
    private String title;
}
