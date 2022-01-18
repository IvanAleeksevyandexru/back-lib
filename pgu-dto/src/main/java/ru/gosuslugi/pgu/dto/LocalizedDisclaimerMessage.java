package ru.gosuslugi.pgu.dto;

import lombok.Data;

@Data
public class LocalizedDisclaimerMessage {
    private String language;
    private String message;
}
