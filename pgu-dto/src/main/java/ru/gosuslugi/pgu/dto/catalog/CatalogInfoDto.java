package ru.gosuslugi.pgu.dto.catalog;

import lombok.Data;

@Data
public class CatalogInfoDto {

    private String targetName;

    private String epguCode;

    private String targetExtId;

    private String targetIconSrc;

    private String passCode;
}
