package ru.gosuslugi.pgu.dto;

import lombok.Data;

@Data
public class RateLimitDescriptor {

    private Long ttl;

    private Long limit;

}
