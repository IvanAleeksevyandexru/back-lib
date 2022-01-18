package ru.gosuslugi.pgu.dto.esep;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FileCertificatesUserInfoResponse {
    private List<CertificateInfoDto> certificateInfoDtoList = new ArrayList<>();
}
