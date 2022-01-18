package ru.gosuslugi.pgu.dto;

import lombok.Getter;

public enum DisclaimerLevel {
    CRITICAL("Не удалось загрузить страницу"),
    ERROR("Сервис недоступен"),
    WARN("Технические работы"),
    INFO("Важно");

    @Getter
    private final String header;

    DisclaimerLevel(String header) {
        this.header = header;
    }
}
