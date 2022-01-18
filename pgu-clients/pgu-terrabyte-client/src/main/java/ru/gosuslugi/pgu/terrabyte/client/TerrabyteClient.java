package ru.gosuslugi.pgu.terrabyte.client;

import ru.gosuslugi.pgu.terrabyte.client.model.DigestServiceDto;
import ru.gosuslugi.pgu.terrabyte.client.model.FileInfo;

import java.util.List;

public interface TerrabyteClient {

    /**
     * Метод для запроса списка файлов загруженных в сервис "Терабайт"
     * Request type: GET
     * URL: /api/storage/v1/files/orderId
     */
    List<FileInfo> getAllFilesByOrderId(Long orderId, Long userId, String token);

    FileInfo getFileInfo(FileInfo fileInfo, Long userId, String token);

    FileInfo deleteFile(FileInfo fileInfo, Long userId, String token);

    byte[] getFile(FileInfo fileInfo, Long userId, String token);

    /**
     * Скачивание файла с терабайт
     * @param fileUid идентификатор файла
     * @return массив байт указанного файла
     */
    byte[] getFile(long fileUid);

    void internalSaveFile(byte[] fileBody, String fileName, String mnemonic, String mimeType, long objectId, int objectTypeId);

    void deleteFile(long objectId, int objectTypeId, String mnemonic);

    void copyFile(FileInfo src, FileInfo trg);

    String getDigestValue(DigestServiceDto digestServiceDto);

}
