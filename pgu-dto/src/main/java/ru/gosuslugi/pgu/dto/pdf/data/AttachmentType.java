package ru.gosuslugi.pgu.dto.pdf.data;

import lombok.AllArgsConstructor;

/**
 * Порядок обработки файлов как вложений СМЭВ-запроса.
 */
@AllArgsConstructor
public enum AttachmentType {

    /**
     * Файл отправляется в запросе к СМЭВ в качестве вложения.
     */
    LK("lk"),

    /**
     * Содержимое файла используется как тело запроса к СМЭВ.
     */
    REQUEST("request"),

    /**
     * Файл не отправляется в запросе к СМЭВ в качестве вложения.
     */
    SEND_SMEV_FORBIDDEN("send_smev_forbidden");

    private String value;

}
