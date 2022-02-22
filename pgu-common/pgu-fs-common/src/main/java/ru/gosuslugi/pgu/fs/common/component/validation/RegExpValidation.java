package ru.gosuslugi.pgu.fs.common.component.validation;

import ru.gosuslugi.pgu.dto.ApplicantAnswer;
import ru.gosuslugi.pgu.components.ValidationUtil;
import ru.gosuslugi.pgu.dto.descriptor.FieldComponent;
import ru.gosuslugi.pgu.fs.common.utils.AnswerUtil;

import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpValidation implements ValidationRule {

    private Pattern pattern;
    private String errorMessage;

    /**
     * При дефолтном конструкторе regexp правила для валидации берутся из описания компонента
     */
    public RegExpValidation() {
    }

    public RegExpValidation(Pattern pattern, String errorMessage) {
        this.pattern = pattern;
        this.errorMessage = errorMessage;
    }

    @Override
    public Map.Entry<String, String> validate(Map.Entry<String, ApplicantAnswer> entry, FieldComponent fieldComponent) {
        String value = AnswerUtil.getValue(entry);
        if (pattern == null) {
            return ValidationUtil.validateRegExp(entry.getKey(), value, fieldComponent);
        }
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            return new AbstractMap.SimpleEntry<>(entry.getKey(), errorMessage);
        }
        return null;
    }
}
