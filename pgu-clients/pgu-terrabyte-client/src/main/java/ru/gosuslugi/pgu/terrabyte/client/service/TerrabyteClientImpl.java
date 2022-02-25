package ru.gosuslugi.pgu.terrabyte.client.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.exception.PguException;
import ru.gosuslugi.pgu.core.storageservice.model.FileKey;
import ru.gosuslugi.pgu.core.storageservice.model.remote.DeleteFileRequest;
import ru.gosuslugi.pgu.terrabyte.client.TerrabyteClient;
import ru.gosuslugi.pgu.terrabyte.client.config.TerrabyteClientProperties;
import ru.gosuslugi.pgu.terrabyte.client.model.CopyFileRequestDto;
import ru.gosuslugi.pgu.terrabyte.client.model.DigestServiceDto;
import ru.gosuslugi.pgu.terrabyte.client.model.FileInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;

@Slf4j
@RequiredArgsConstructor
public class TerrabyteClientImpl implements TerrabyteClient {

    private static final String REQUEST_ALL = "/files/{orderId}";
    private static final String GET_SPECIFIC_FILE = "/files/{orderId}/{objectTypeId}?mnemonic={mnemonic}";
    private static final String GET_DIGEST = "/digest";
    private static final String GET_FILE = "/files/{orderId}/{objectTypeId}/download?mnemonic={mnemonic}";
    private static final String GET_FILE_BY_UID = "/file-content/{fileUid}";
    private static final String UPLOAD_FILE = "/upload?mnemonic={mnemonic}&name={name}&objectId={objectId}&objectType={objectType}&mimeType={mimeType}&uuid={uuid}&hasSign=false&chunk=0&chunks=1";
    private static final String DELETE = "/delete";
    private static final String COPY_FILE = "/copy";
    private static final String ZIP_FILES = "/zip";


    private final RestTemplate restTemplate;
    private final TerrabyteClientProperties properties;

    @Override
    public List<FileInfo> getAllFilesByOrderId(Long orderId, Long userId, String token) {
        try {
            debug(log, () -> format("Запрос сведений по файлам в сервис Терабайт для orderId = %s", orderId));
            String url = buildUrl(properties.getStorageUrl(), REQUEST_ALL);
            ResponseEntity<List<FileInfo>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(tokenToHeaders(userId, token, MediaType.APPLICATION_JSON)),
                    new ParameterizedTypeReference<>() {},
                    Map.of("orderId", orderId));
            if (HttpStatus.OK == response.getStatusCode()) {
                return response.getBody();
            }
            return List.of();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("При запросе файлов для orderId = {} из Террабайт возвращен статус 404", orderId, e);
            return Collections.emptyList();
        } catch (RestClientException | PguException e) {
            log.error("Ошибка при запросе сведений по файлам в сервис Терабайт для orderId = {}", orderId, e);
            return List.of();
        }
    }

    @Override
    public FileInfo getFileInfo(FileInfo fileInfo, Long userId, String token) {
        return requestFile(fileInfo, HttpMethod.GET, userId, token);
    }

    @Override
    public FileInfo deleteFile(FileInfo fileInfo, Long userId, String token) {
        return requestFile(fileInfo, HttpMethod.DELETE, userId, token);
    }

    @Override
    public byte[] getFile(FileInfo fileInfo, Long userId, String token) {
        return restTemplate
                .exchange(
                        properties.getStorageUrl() + GET_FILE,
                        HttpMethod.GET,
                        new HttpEntity<>(tokenToHeaders(userId, token, MediaType.ALL)),
                        byte[].class,
                        getRequestParameters(fileInfo))
                .getBody();
    }

    /**
     * Скачивание файла с терабайт
     * @param fileUid идентификатор файла
     * @return массив байт указанного файла
     */
    @Override
    public byte[] getFile(long fileUid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate
                .exchange(
                        properties.getInternalStorageUrl() + GET_FILE_BY_UID,
                        HttpMethod.GET,
                        entity,
                        byte[].class,
                        Map.of("fileUid", fileUid)
                ).getBody();
    }

    private Map<String, String> getRequestParameters(FileInfo fileInfo) {
        return Map.of(
                "orderId", String.valueOf(fileInfo.getObjectId()),
                "objectTypeId", String.valueOf(fileInfo.getObjectTypeId()),
                "mnemonic", fileInfo.getMnemonic()
        );
    }

    @Override
    public void internalSaveFile(byte[] fileBody, String fileName, String mnemonic, String mimeType, long objectId, int objectTypeId) {
        final String uuid = UUID.randomUUID().toString();
        try {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            Map<String, Object> params = Map.of("mnemonic", mnemonic,
                    "name", fileName,
                    "objectId", objectId,
                    "objectType", objectTypeId,
                    "mimeType", mimeType,
                    "uuid", uuid);


            ByteArrayResource contentsAsResource = new ByteArrayResource(fileBody) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            body.add("file", contentsAsResource);

            HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(properties.getInternalStorageUrl() + UPLOAD_FILE, req, String.class, params);
        } catch (Exception e) {
            throw new ExternalServiceException("Couldn't save File objectId " + objectId + " and mnemonic " + mnemonic + ", code: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(long objectId, int objectTypeId, String mnemonic) {
        try {
            DeleteFileRequest request = new DeleteFileRequest();
            request.setFileKeyList(List.of(new FileKey(objectId, objectTypeId, mnemonic)));

            HttpEntity<DeleteFileRequest> req = new HttpEntity<>(request, new HttpHeaders());
            restTemplate.postForEntity(properties.getInternalDataserviceUrl() + DELETE, req, String.class);
        } catch (Exception e) {
            throw new ExternalServiceException("Couldn't delete file objectId " + objectId + " and type " + objectTypeId + ", code: " + e.getMessage(), e);
        }
    }

    @Override
    public void copyFile(FileInfo src, FileInfo trg) {
        var copyFileRequest = getCopyFileRequestDto(src, trg);
        try {
            HttpEntity<CopyFileRequestDto> req = new HttpEntity<>(copyFileRequest, new HttpHeaders());
            restTemplate.postForEntity(properties.getInternalDataserviceUrl() + COPY_FILE, req, String.class);
        } catch (Exception e) {
            throw new ExternalServiceException("Couldn't copy files by request" + copyFileRequest + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void copyFile(FileInfo src, FileInfo trg, Long userId, String token) {
        var copyFileRequest = getCopyFileRequestDto(src, trg);
        try {
            HttpEntity<CopyFileRequestDto> req = new HttpEntity<>(copyFileRequest, tokenToHeaders(userId, token, MediaType.APPLICATION_JSON));
            restTemplate.postForEntity(properties.getStorageUrl() + "/files" + COPY_FILE, req, String.class);
        } catch (Exception e) {
            throw new ExternalServiceException("Couldn't copy files by request" + copyFileRequest + ": " + e.getMessage(), e);
        }
    }

    private CopyFileRequestDto getCopyFileRequestDto(FileInfo src, FileInfo trg) {
        return new CopyFileRequestDto(List.of(
                new CopyFileRequestDto.CopyPair(
                        new CopyFileRequestDto.FileKey(src.getObjectId(), src.getObjectTypeId(), src.getMnemonic(), false),
                        new CopyFileRequestDto.FileKey(trg.getObjectId(), trg.getObjectTypeId(), trg.getMnemonic(), false))));
    }

    @Override
    public String getDigestValue(DigestServiceDto digestServiceDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.CONTENT_TYPE, List.of("application/json;charset=UTF-8"));
        HttpEntity<DigestServiceDto> request = new HttpEntity<>(digestServiceDto, httpHeaders);
        ResponseEntity<DigestServiceDto> response = restTemplate.postForEntity( properties.getInternalStorageUrl() + GET_DIGEST, request , DigestServiceDto.class );
        return response.getBody().getFiles().get(0).getDigest();
    }

    private FileInfo requestFile(FileInfo fileInfo, HttpMethod httpMethod, Long userId, String token) {
        return restTemplate
                .exchange(
                        properties.getStorageUrl() + GET_SPECIFIC_FILE,
                        httpMethod,
                        new HttpEntity<>(tokenToHeaders(userId, token, MediaType.APPLICATION_JSON)),
                        FileInfo.class,
                        getRequestParameters(fileInfo))
                .getBody();
    }

    private HttpHeaders tokenToHeaders(Long userId, String token, MediaType acceptType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(acceptType));
        headers.add("cookie", "u=" + userId);
        headers.add("cookie", "acc_t=" + token);
        return headers;
    }

    protected String buildUrl(String... args){
        return String.join("/", args);
    }

    @Override
    public FileInfo zipOrderFiles(Long orderId, boolean delete, String fileName, String mnemonic, Long userId, String token) {
        final HttpHeaders headers = new HttpHeaders();

        Map<String, Object> params = Map.of(
                "delete", delete,
                "fileName", fileName,
                "objectId", orderId,
                "objectType", 2,
                "mnemonic", mnemonic
        );

        List<String> body = new ArrayList<>();
        for (FileInfo file: getAllFilesByOrderId(orderId, userId, token)) {
            body.add("terrabyte://00/" + file.getObjectId() + "/" + file.getMnemonic() + "/" + file.getObjectTypeId());
        }
        HttpEntity<List<String>> req = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                properties.getInternalStorageUrl() + ZIP_FILES + "?delete={delete}&fileName={fileName}&objectId={objectId}&objectType={objectType}&mnemonic={mnemonic}",
                req,
                String.class,
                params);

        FileInfo file = new FileInfo();
        file.setMnemonic(mnemonic);
        file.setObjectId(orderId);
        file.setObjectTypeId(2);
        return getFileInfo(file, userId, token);
    }

    @Override
    public FileInfo zipFiles(Long objectId, boolean delete, String fileName, String mnemonic, List<FileInfo> fileList, Long userId, String token) {
        final HttpHeaders headers = new HttpHeaders();

        Map<String, Object> params = Map.of(
                "delete", delete,
                "fileName", fileName,
                "objectId", objectId,
                "objectType", 2,
                "mnemonic", mnemonic
        );

        List<String> body = new ArrayList<>();
        for (FileInfo file: fileList) {
            body.add("terrabyte://00/" + file.getObjectId() + "/" + file.getMnemonic() + "/" + file.getObjectTypeId());
        }
        HttpEntity<List<String>> req = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                properties.getInternalStorageUrl() + ZIP_FILES + "?delete={delete}&fileName={fileName}&objectId={objectId}&objectType={objectType}&mnemonic={mnemonic}",
                req,
                String.class,
                params);

        FileInfo file = new FileInfo();
        file.setMnemonic(mnemonic);
        file.setObjectId(objectId);
        file.setObjectTypeId(2);
        return getFileInfo(file, userId, token);
    }
}
