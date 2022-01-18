package ru.gosuslugi.pgu.dto.esep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateInfoDto {
    private List<CertificateUserInfoDto> certificateUserInfoList;
    private String fileAccessCode;
}
