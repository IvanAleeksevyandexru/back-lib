package ru.gosuslugi.pgu.dto.lk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Класс представляет запись с данными в таблице LK.SC_DATA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LkDataMessage {

    /**
     * Имя поля, можно использовать имя компонента из json услуги
     */
    @NotEmpty
    private String fieldName;

    /**
     * Значение поля
     */
    @NotEmpty
    private String fieldValue;

    /**
     * Мнемоника поля
     */
    private String fieldMnemonic;

}
