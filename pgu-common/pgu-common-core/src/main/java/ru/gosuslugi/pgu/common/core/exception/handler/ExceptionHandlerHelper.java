package ru.gosuslugi.pgu.common.core.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.gosuslugi.pgu.common.core.exception.PguQuietException;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessage;
import ru.gosuslugi.pgu.common.logging.service.SpanService;

import static org.springframework.util.StringUtils.isEmpty;
import static ru.gosuslugi.pgu.common.logging.service.SpanService.EXCEPTION_TAG;
import static ru.gosuslugi.pgu.common.logging.service.SpanService.MESSAGE_TAG;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerHelper {

    private final SpanService spanService;

    public <R extends ErrorMessage> ResponseEntity<R> handle(
            Exception ex,
            ErrorMessageFactory<R> errorMessageFactory,
            boolean withTraceId
    ) {

        ResponseStatus responseStatus = extractResponseStatus(ex);
        final var err = errorMessageFactory.createErrorMessage(
                !isEmpty(responseStatus.reason()) ? responseStatus.reason() : responseStatus.value().getReasonPhrase(),
                responseStatus.value().name()
        );

        if (!(ex instanceof PguQuietException)) {
            log.error(err.toString(), ex);
        }
        if (withTraceId) {
            err.setTraceId(spanService.getCurrentTraceId());
        }
        spanService.addTagToSpan(EXCEPTION_TAG, ex.getClass().getName());
        spanService.addTagToSpan(MESSAGE_TAG, err.toString());
        return new ResponseEntity<>(err, responseStatus.value());
    }

    public <R extends ErrorMessage> ResponseEntity<R> handleModal(Exception ex, ErrorMessageFactory<R> errorMessageFactory) {
        return handle(ex, errorMessageFactory, false);
    }

    public <R extends ErrorMessage> ResponseEntity<R> handle(Exception ex, ErrorMessageFactory<R> errorMessageFactory) {
        return handle(ex, errorMessageFactory, true);
    }

    static ResponseStatus extractResponseStatus(Exception e) {
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        Assert.notNull(responseStatus, "Cannot find ResponseStatus annotation");
        Assert.notNull(responseStatus.value(), "ResponseStatus with empty status code");
        return responseStatus;
    }

}
