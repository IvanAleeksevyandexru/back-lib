package ru.gosuslugi.pgu.terrabyte.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copied from ru.gosuslugi.pgu.core.storageservice.ws.CopyFileRequest (pgu-model-client)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopyFileRequestDto {

    private List<CopyPair> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CopyPair {

        private FileKey srcFile;
        private FileKey trgFile;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileKey {

        private long objectId;
        private long objectType;
        private String mnemonic;
        private boolean fdcOnly;

    }

}
