package ru.gosuslugi.pgu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Описание файла в террабайт
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerrabyteFileInfo {
    /** идентификатор */
    private long uid;
    /** расширение файла */
    private String fileExt;
}
