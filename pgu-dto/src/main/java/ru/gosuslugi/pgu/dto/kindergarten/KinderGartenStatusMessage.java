package ru.gosuslugi.pgu.dto.kindergarten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сообщение от СМЭВ-адаптера по дет.садам и статус.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KinderGartenStatusMessage {
    /** Статус-код состояния. */
    private KinderGartenHandlerStatus status;
    /** Сообщение в случае исключительной ситуации. */
    private String internalExceptionMessage;
}
