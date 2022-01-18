package ru.gosuslugi.pgu.dto;

import lombok.Data;
import ru.gosuslugi.pgu.dto.pdf.data.FileDescription;

import java.util.List;
import java.util.Map;

/**
 * Описание поведения услуги для sp-adapter
 */
@Data
public class Descriptor {

    /** Название вложения бизнес-xml */
    private String businessXmlName;

    /** Добавочные заголовки для запроса в sp-service */
    private Map<String, String> replacedHeaders;

    /** строка на которую меняется serviceId при вызове sp */
    private String serviceCustomId;

    /**
     * Параметр, указывающий на то, что нужно всегда генерить сервисную pdf-ку
     * даже если пользователь реально не отправляет запрос в СМЭВ
     */
    private Boolean alwaysAttachServicePdf;

    /** Содержит перечисление генерируемых файлов. Будут сгенерированы только они. */
    private List<FileDescription> files;
    /**
     * Параметр, указывающий на то, что нужно отключить значения
     * по умолчанию digestValue при невозможности их получения от сервиса Террабайт
     */
    private boolean defaultDigestValueEnabled = true;
    /**
     * Параметр указывающий специфичное имя файла при отправке дополнительной pdf-ки
     * с разбивкой по ролям и префиксам файлов
     */
    private Map<String,Map<String, String>> additionalPdfName;
    /**
     * Флаг отправки req_preview.pdf в smev
     */
    private boolean regPreviewSendToSP = false;
}
