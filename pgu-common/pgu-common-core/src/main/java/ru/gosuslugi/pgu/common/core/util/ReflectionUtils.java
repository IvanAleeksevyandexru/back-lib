package ru.gosuslugi.pgu.common.core.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.reflect.FieldUtils;

import java.util.ArrayList;
import java.util.Arrays;

@UtilityClass
public class ReflectionUtils {

    /**
     * Последовательно читает вложенные поля из объекта, в т.ч. приватные. Пример использования:
     *   readNestedField(restTemplate, "requestFactory", "httpClient", "connManager")
     *
     * В случае любых проблем (нет такого поля, пустое значение промежуточного поля) выбрасывает exceptions.
     */
    public static Object readNestedField(Object target, String field1, String... fields) throws IllegalAccessException {
        var fieldNames = new ArrayList<String>();
        fieldNames.add(field1);
        fieldNames.addAll(Arrays.asList(fields));

        Object result = target;
        for (String field : fieldNames) {
            result = FieldUtils.readField(result, field, true);
        }
        return result;
    }
}
