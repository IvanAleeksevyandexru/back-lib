package ru.gosuslugi.pgu.dto;

import lombok.Data;

import java.util.List;

@Data
public class PortalDisclaimer {
    Long id;
    String mnemonic;
    String code;
    String region;
    List<LocalizedDisclaimerMessage> messages;
    String level;
    Boolean notificationEnabled;
    Boolean isPriority;
    Boolean isHidden;
    String title;
}
