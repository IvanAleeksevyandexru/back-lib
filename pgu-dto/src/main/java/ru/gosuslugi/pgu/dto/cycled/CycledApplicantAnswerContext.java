package ru.gosuslugi.pgu.dto.cycled;
import lombok.Data;

/**
 * Контекст циклического ответа
 */
@Data
public class CycledApplicantAnswerContext {

    /** Циклический ответ */
    private CycledApplicantAnswerItem cycledApplicantAnswerItem;
}