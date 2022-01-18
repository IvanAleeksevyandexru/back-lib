package ru.gosuslugi.pgu.dto.esep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PrepareSignResponse {
    private String operationID;
    private String url;
    private List<SignedFileInfo> signedFileInfos = new ArrayList<>();
}
