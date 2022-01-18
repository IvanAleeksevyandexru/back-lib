package ru.gosuslugi.pgu.terrabyte.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfo {

    /**
     * При отправке файлов на сервер front-end использует orderId.
     */
    long objectId;
    /**
     * Должен принимать два значения - 2 (аттачмент) и 4 (подпись)
     */
    int objectTypeId;
    /**
     * При отправке файлов на сервер front-end формирует его по правилу
     * {componentId}.{componentType}.{uploadId}.{fileNumber}
     * где componentId - {@link FieldComponent.id}
     * componentType - {@link FieldComponent.type}
     * uploadId - можно найти в атрибутах компонента "attrs":{"uploads":[{"uploadId":"passport_photo","type" ...
     * fileNumber - номер файла начиная с нулевого.
     */
    String mnemonic;

    String fileName;

    /** Идентификатор файла в террабайте */
    Long fileUid;

    String fileExt;

    Long fileSize;
}
