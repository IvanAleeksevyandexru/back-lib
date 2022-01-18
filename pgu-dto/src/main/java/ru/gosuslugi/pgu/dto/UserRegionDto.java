package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Регион пользователя
 * https://jira.egovdev.ru/browse/EPGUCORE-46548
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegionDto {

    /** Название региона в госбаре */
    private String name;

    /** Путь до региона */
    private String path;

    /** Перечень кодов ОКАТО пользователя */
    private List<String> codes;
}
