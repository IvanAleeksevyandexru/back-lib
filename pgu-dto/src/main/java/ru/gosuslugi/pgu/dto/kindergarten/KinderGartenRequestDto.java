package ru.gosuslugi.pgu.dto.kindergarten;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Объединяет параметры, необходимые для запроса во входящую очередь СМЭВ-адаптера.
 */
@Data
@AllArgsConstructor
public class KinderGartenRequestDto {
    /** Запрос по дет.садам в формате xml. */
    @NotNull
    private String xmlRequest;

    /** Версия СМЭВ. */
    @NotNull
    private String smevVersion;

    /** Id заявления. */
    @NotNull
    private Long orderId;

    /** Величина таймаута для rest-клиента. */
    @NotNull
    private Long timeout;
}
