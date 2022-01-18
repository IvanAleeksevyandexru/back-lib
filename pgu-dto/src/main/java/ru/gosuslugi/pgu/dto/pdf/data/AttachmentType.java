package ru.gosuslugi.pgu.dto.pdf.data;

import lombok.AllArgsConstructor;

/**
 * Способы сохранения файла
 */
@AllArgsConstructor
public enum AttachmentType {

    LK("lk"),

    REQUEST("request"),

    HIDDEN("hidden");

    private String value;

}
