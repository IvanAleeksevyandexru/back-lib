package ru.gosuslugi.pgu.dto.ratelimit;


import lombok.Data;

import java.util.Objects;

@Data
public class RateLimitRequest {

    private String version;

    private Long ttl;

    private Long limit;


    public String getVersionOrDefault(String defaultValue){
        if(Objects.isNull(version)){
            return defaultValue;
        }
        return version;
    }

    public Long getTtlOrDefault(Long defaultTtl){
        if(Objects.isNull(ttl)){
            return defaultTtl;
        }
        return ttl;
    }

    public Long getLimitOrDefault(Long defaultLimit){
        if(Objects.isNull(limit)){
            return defaultLimit;
        }
        return limit;
    }
}
