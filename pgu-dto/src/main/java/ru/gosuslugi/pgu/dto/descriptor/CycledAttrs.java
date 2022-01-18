package ru.gosuslugi.pgu.dto.descriptor;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Атрибуты компонента для цикличной обработки
 */
@Data
public class CycledAttrs {
    /** идентификатор цикличного компонента */
    private String cycledAnswerId;

    /** индекс цикличного ответа */
    private String cycledAnswerIndex;

    /**
     * Установлены ли цикличные атрибуты
     * @return {@code true}, если цикличные атрибуты не пустые, иначе - {@code false}
     */
    public boolean isSet() {
        return !StringUtils.isEmpty(cycledAnswerId) && !StringUtils.isEmpty(cycledAnswerIndex);
    }
}
