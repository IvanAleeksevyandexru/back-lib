package ru.gosuslugi.pgu.dto.lk;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Запрос отправки сообщений в ЛК в таблицу LK.SC_DATA
 */
@Data
@AllArgsConstructor
public class SendNotificationRequestDto {

    @NotNull
    private Long orderId;

    @Valid
    @NotEmpty
    private List<LkDataMessage> messages;

}
