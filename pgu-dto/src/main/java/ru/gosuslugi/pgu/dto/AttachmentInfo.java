package ru.gosuslugi.pgu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Class that contains attachement information, such as file mnemonic, upload id
 * This information is used in sp-adapter module
 * in order to link attachments with service processing & SMEV requests
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AttachmentInfo {
    private String uploadId;
    private String uploadMnemonic;
    private String uploadFilename;
    private String objectId;
    private String objectTypeId;

    public AttachmentInfo(String uploadId, String uploadMnemonic, String uploadFilename, String objectId, String objectTypeId) {
        this.uploadId = uploadId;
        this.uploadMnemonic = uploadMnemonic;
        this.uploadFilename = uploadFilename;
        this.objectId = objectId;
        this.objectTypeId = objectTypeId;
    }
}
