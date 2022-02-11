package ru.gosuslugi.pgu.components.regex;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Хранилище предварительно скомпилированных паттернов, которые возвращаются по их регулярного выражению.
 * Ограничения: не умеет использовать литералы, независимость от регистра и прочие флаги, кроме случаев, когда флаг - часть паттерна.
 * <ul>Пример:
 *     <li>{@code ^(?i)[a-z]+$} - регистронезависимое регулярное выражение, строка может состоять только из букв</li>
 *     <li>{@code ^(?m)[a-z]+day[0-9]\nmonth[0-9]} - многострочное регулярное выражение, строка может содержать символы \n \r \t</li>
 * </ul>
 */
public class RegExpContext {

    private RegExpContext() {
    }

    /**
     * Мапка скомпилированных паттернов.
     * В случае превышения максимального размера вытесняется самый старый паттерн.
     * Ограничение на размер сделано на всякий случай, на момент написания кода было 405 уникальных регулярок в json.
     */
    private final static ConcurrentMap<String, Pattern> CONTAINER = new ConcurrentHashMap<>();

    /**
     * Осуществляет поиск по строке паттерна и применяет функцию к найденному или вновь созданному паттерну.
     * @param regex строка регулярного выражения
     * @param matchFunction функция, которую применяем к скомпилированному паттерну
     * @param <T> параметр-тип результата
     * @return результат работы применяемой к паттерну функции
     */
    public static  <T> T getValueByRegex(String regex, Function<Pattern, T> matchFunction) {
        Pattern pattern = getPattern(regex);
        return matchFunction.apply(pattern);
    }

    /**
     * Осуществляет поиск паттерна в хранилище строк и матчит строку по созданному паттерну.
     * Создан для избегания боксинга/анбоксинга при использовании {@link #getValueByRegex(String, Function)}.
     * @param input входная строка для матчинга по скомпилированному паттерну
     * @param regex строка регулярного выражения
     * @return результат матчинга по паттерну функции
     */
    public static boolean matchesByRegex(String input, String regex) {
        Pattern pattern = getPattern(regex);
        return pattern.matcher(input).matches();
    }

    static int getSize() {
        return CONTAINER.size();
    }

    static void clear() {
        CONTAINER.clear();
    }

    /**
     * Возвращает скомпилированный паттерн по регулярному выражению.
     * Если регулярка содержится в мапе, то сразу возвращает готовый паттерн,
     * в противном случае компилирует и помещает в мапу перед возвратом.
     *
     * @param key строка регулярного выражения
     * @return скомпилированный паттерн
     * @throws PatternSyntaxException если регулярное выражение невалидно и не может быть применено
     */
    private static Pattern getPattern(String key) {
        return CONTAINER.computeIfAbsent(key, k -> Pattern.compile(key));
    }
}
