package ru.gosuslugi.pgu.dto.esep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateUserInfoDto {
    private String commonName;
    private String snils;
    private String inn;
    private String ogrn;
    private String ogrnip;
}
