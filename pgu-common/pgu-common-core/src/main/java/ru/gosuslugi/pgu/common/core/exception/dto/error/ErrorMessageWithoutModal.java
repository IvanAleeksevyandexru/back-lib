package ru.gosuslugi.pgu.common.core.exception.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import ru.gosuslugi.pgu.common.core.exception.PguException;

@Data
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessageWithoutModal extends ErrorMessage {

    public ErrorMessageWithoutModal() {
        super();
    }

    public ErrorMessageWithoutModal(String responseStatus, String reason, PguException ex) {
        super(responseStatus, reason, ex);
    }
}
