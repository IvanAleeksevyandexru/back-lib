package ru.gosuslugi.pgu.dto;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class PguTimer {
    private String code;
    private boolean isActive;
    private int duration;
    private TimeUnit unit;
    private String startTime;
    private String expirationTime;
    private String currentTime;
}
