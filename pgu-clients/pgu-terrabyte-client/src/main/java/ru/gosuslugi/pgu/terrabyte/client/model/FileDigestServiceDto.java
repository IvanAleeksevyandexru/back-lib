package ru.gosuslugi.pgu.terrabyte.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gosuslugi.pgu.dto.AttachmentInfo;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class FileDigestServiceDto {
    private String fileLink;
    private String digest;

    public FileDigestServiceDto(String objectId, String objectTypeId, String uploadMnemonic) {
        this.digest = "";
        this.fileLink = "terrabyte://00/" + objectId + "/" + uploadMnemonic + "/" + objectTypeId;
    }
}
