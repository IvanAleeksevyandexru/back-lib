package ru.gosuslugi.pgu.components.descriptor.converter;

import org.apache.logging.log4j.util.Strings;

import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isBlank;
import static ru.gosuslugi.pgu.components.regex.RegExpContext.getValueByRegex;

/**
 * Replace subsrtings by regex
 */
public class StringReplaceConverter implements Converter {

    private static final String REPLACEMENT_ATTR = "for";

    @Override
    public Object convert(Object value, Map<String, Object> attrs) {
        String result = Optional.ofNullable(value).map(Object::toString).orElse(Strings.EMPTY);
        var optional = Optional.ofNullable(attrs.get(REPLACEMENT_ATTR));
        if (isBlank(result) || optional.isEmpty()) {
            return result;
        }

        String[] res = new String[] {result};

        if (optional.get() instanceof Map) {
            ((Map<String, String>) optional.get())
                    .forEach((k, v) -> res[0] = getValueByRegex(k, pattern -> pattern.matcher(res[0]).replaceAll(v)));
        }

        return res[0];
    }
}
