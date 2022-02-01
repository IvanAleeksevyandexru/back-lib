package ru.gosuslugi.pgu.ratelimit.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gosuslugi.pgu.common.core.exception.PguException;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitServiceException extends PguException {

    private final String reason;

    public RateLimitServiceException(String message, String reason, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public RateLimitServiceException(String message, Long limit, Throwable cause) {
        this(message, String.format("Вы уже запросили проверку %d раз за последнее время, подождите 10 минут и попробуйте снова", limit), cause);
    }

    public String getReason() {
        return reason;
    }
}
