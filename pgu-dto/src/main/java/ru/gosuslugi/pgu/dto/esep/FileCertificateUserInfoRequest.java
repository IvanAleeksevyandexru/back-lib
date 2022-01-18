package ru.gosuslugi.pgu.dto.esep;

import lombok.Data;

import java.util.List;

@Data
public class FileCertificateUserInfoRequest {
    private List<String> fileAccessCodes;
}