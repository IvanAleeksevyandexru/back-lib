package ru.gosuslugi.pgu.common.rendering.render.template.function;

import java.util.Optional;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Реализует вспомогательные методы работы с текстом, содержащим XML-разметку.
 */
public class XmlService {

    /**
     * Преобразует HTML-мнемоники, встречающиеся в синтаксисе разметки XML в соответствующие Unicode
     * символы.
     * <p>
     * Для преобразования value в строку вызывается {@link Object#toString()}.
     *
     * @param value значение. Может быть null.
     * @return выходное значение. null, если value был null.
     * @see StringEscapeUtils#unescapeXml(String)
     */
    public String unescape(Object value) {
        return Optional.ofNullable(value)
                .map(Object::toString)
                .map(StringEscapeUtils::unescapeXml)
                .orElse(null);
    }
}
