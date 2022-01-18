package ru.gosuslugi.pgu.common.core.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import ru.gosuslugi.pgu.common.core.exception.EntityNotFoundException;
import ru.gosuslugi.pgu.common.core.exception.ExternalServiceException;
import ru.gosuslugi.pgu.common.core.exception.JsonParsingException;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessage;
import ru.gosuslugi.pgu.common.core.exception.dto.error.ErrorMessageWithoutModal;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;

/**
 * Обработчик ошибок для клиентов REST сервисов на основе RestTemplate
 */
@Slf4j
public class RestResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.ACCEPTED;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        handleError(null, null, response);
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        final HttpStatus status = response.getStatusCode();
        final String body = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
        ErrorMessage errorBody = retrieveErrorMessage(body);
        final String message = String.format("Response code: %s %s. Body: [%s], url: [%s], method: [%s]",
            status.value(), status.getReasonPhrase(), body, url, method);

        if (status == HttpStatus.NOT_FOUND) {
            throw new EntityNotFoundException(errorBody, "External entity not found. " + message);
        }
        throw new ExternalServiceException(errorBody, "Error response from external service. " + message, status);
    }

    private ErrorMessage retrieveErrorMessage(String responseBody) {
        try {
            JsonNode json = JsonProcessingUtil.getObjectMapper().readTree(responseBody);
            if (json.isArray()) {
                List<ErrorMessage> errorList;
                errorList = JsonProcessingUtil.fromJson(responseBody, new TypeReference<>() {});
                return errorList.get(0);
            }
            return JsonProcessingUtil.fromJson(responseBody, new TypeReference<>() {});
        } catch (JsonProcessingException | JsonParsingException e) {
            debug(log, () -> String.format("JsonProcessingException while parsing '%s', return empty ErrorMessage", responseBody));
        }
        return new ErrorMessageWithoutModal();
    }

}
