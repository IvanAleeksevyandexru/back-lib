package ru.gosuslugi.pgu.common.rendering.render.template.function;

import static java.util.Objects.isNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Конкатенирует строковые представления объектов в строку.
 */
public class StringService {

    /**
     * Конкатенирует строковые представления объектов в строку, используя разделитель.
     * <p>
     * Если один из объектов null или является пустой строкой, он не попадает в список.
     *
     * @param delimiter разделитель.
     * @param objects объекты.
     * @return результирующая строка.
     */
    public String printWithDelimiter(String delimiter, Object... objects) {
        return Stream.of(objects)
                .filter(obj -> !isNull(obj))
                .map(Object::toString)
                .filter(str -> !str.isBlank())
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Присоединяет строковые представления objects через delimiter, до тех пор, пока не встретится
     * или null, или объект с пустым строковым представлением.
     *
     * @param delimiter разделитель.
     * @param objects объекты.
     * @return результирующая строка.
     */
    public String printLeftJoinWithDelimiter(String delimiter, Object... objects) {
        AtomicBoolean leftExisted = new AtomicBoolean(true);
        return Stream.of(objects)
                .filter(
                        obj -> {
                            leftExisted.set(leftExisted.get() && !isNull(obj));
                            return leftExisted.get();
                        }
                )
                .map(Object::toString)
                .filter(
                        str -> {
                            leftExisted.set(leftExisted.get() && !str.isBlank());
                            return leftExisted.get();
                        }
                )
                .collect(Collectors.joining(delimiter));
    }
}