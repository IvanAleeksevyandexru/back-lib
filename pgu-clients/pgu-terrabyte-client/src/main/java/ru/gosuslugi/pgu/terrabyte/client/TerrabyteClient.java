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

    void copyFile(FileInfo src, FileInfo trg, Long userId, String token);

    String getDigestValue(DigestServiceDto digestServiceDto);

    /**
     * Архивируем и сохраняем файлы ордера в ZIP
     * http://kuber.gosuslugi.local:15294/terrabyte-uploader/swagger-ui.html#/%D0%92%D0%BD%D1%83%D1%82%D1%80%D0%B5%D0%BD%D0%BD%D0%B8%D0%B5%20%D0%BC%D0%B5%D1%82%D0%BE%D0%B4%D1%8B%20%D0%B4%D0%BB%D1%8F%20%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B%20%D1%81%20%D1%84%D0%B0%D0%B9%D0%BB%D0%B0%D0%BC%D0%B8/zipUsingPOST
     * @param orderId Идентификатор ордера
     * @param fileName Имя итогового zip'а
     * @param mnemonic Часть мненмоники для zip'а
     * @param userId ИД пользователя
     * @param token Токен пользователя
     * @return FileInfo
     */
    FileInfo zipOrderFiles(Long orderId, boolean delete, String fileName, String mnemonic, Long userId, String token);

    FileInfo zipFiles(Long objectId, boolean delete, String fileName, String mnemonic, List<FileInfo> fileList, Long userId, String token);
}
