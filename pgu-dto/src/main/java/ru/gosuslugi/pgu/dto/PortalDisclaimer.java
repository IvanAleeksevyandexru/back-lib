package ru.gosuslugi.pgu.dto;

import lombok.Data;

import java.util.List;

@Data
public class PortalDisclaimer {
    private Long id;
    private String mnemonic;
    private String code;
    private String region;
    private List<LocalizedDisclaimerMessage> messages;
    private String level;
    private Boolean notificationEnabled;
    private Boolean isPriority;
    private Boolean isHidden;
}
