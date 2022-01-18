package ru.gosuslugi.pgu.terrabyte.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * Тип загруженного в систимму Терабайт файла.
 *
 */
@AllArgsConstructor
public enum FileType {

    ATTACHMENT(2), SIGNATURE(4);

    @Getter
    private final int type;

}