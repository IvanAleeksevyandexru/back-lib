package ru.gosuslugi.pgu.dto.kindergarten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Ответное сообщение от СМЭВ-адаптера по дет.садам.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KinderGartenResponseDto {
    /** Ответ от СМЭВ по дет.садам в формате xml. */
    private String xmlResponse;
    /** Сообщение внутренней ошибки и её статус-код. */
    private KinderGartenStatusMessage message;


    public KinderGartenResponseDto(String xmlResponse, KinderGartenHandlerStatus status, String internalExceptionMessage) {
        message = new KinderGartenStatusMessage(status, internalExceptionMessage);
        this.xmlResponse = xmlResponse;
    }
}
