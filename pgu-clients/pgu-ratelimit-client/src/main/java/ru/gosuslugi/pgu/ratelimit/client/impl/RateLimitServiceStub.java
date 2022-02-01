package ru.gosuslugi.pgu.ratelimit.client.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gosuslugi.pgu.ratelimit.client.RateLimitService;

@Slf4j
@NoArgsConstructor
public class RateLimitServiceStub implements RateLimitService {

        @Override
        public void apiCheck(String key) {
                log.debug("RateLimitServiceStub userKey: " + key);
        }
}
