package ru.gosuslugi.pgu.dto.util;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import ru.gosuslugi.pgu.dto.ApplicantAnswer;
import ru.gosuslugi.pgu.dto.ScenarioDto;

import static java.util.Objects.isNull;

public class DraftUtil {

    /**
     * Получение ответа заявителя по id поля
     * @param scenarioDto черновик
     * @param field id поля ответа
     * @return Ответ заявителя
     */
    public static ApplicantAnswer getApplicantAnswer(ScenarioDto scenarioDto, String field) {
        // обычный ответ
        ApplicantAnswer applicantAnswer = scenarioDto.getApplicantAnswerByFieldId(field);
        if (applicantAnswer == null) {
            applicantAnswer = scenarioDto.getCurrentValue().get(field);
        }
        return applicantAnswer;
    }

    /**
     * Получение в виде строки ответа заявителя по ссылке в Json дереве
     * @param scenarioDto черновик
     * @param linkToApplicantAnswer ссылка вида answ1.value.answer2...
     * @return
     */
    public static String getValueByLink(ScenarioDto scenarioDto, String linkToApplicantAnswer) {
        int indexOfFirstDot = linkToApplicantAnswer.indexOf('.');
        String fieldId = linkToApplicantAnswer.substring(0, indexOfFirstDot);

        ApplicantAnswer applicantAnswer = getApplicantAnswer(scenarioDto, fieldId);
        if (isNull(applicantAnswer)) {
            return null;
        }
        String answerValue = applicantAnswer.getValue();

        try {
            // Если построили DocumentContext, значит значение ответа является валидным JSON.
            // Берем значение узла.
            DocumentContext jsonContext = JsonPath.parse(answerValue);
            String pathToProperty = linkToApplicantAnswer.substring(indexOfFirstDot + 1);

            return jsonContext.read(pathToProperty);
        } catch (Exception ignored) {
            // Ответ не является JSON. Возвращаем строковое представление ответа.
            return answerValue;
        }
    }

}