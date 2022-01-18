package ru.gosuslugi.pgu.dto.ratelimit;

import lombok.Data;

@Data
public class RateLimitOverHeadDto {

    private String serviceId;

    private String orgId;

    private String userId;

    private RateLimitRequest rateLimitRequest;
}
