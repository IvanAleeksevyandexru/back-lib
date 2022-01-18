package ru.gosuslugi.pgu.terrabyte.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class DigestServiceDto {
    private String algorithm;
    private List<FileDigestServiceDto> files;

    public DigestServiceDto(String algorithm, List<FileDigestServiceDto> files) {
        this.algorithm = algorithm;
        this.files = files;
    }
}
