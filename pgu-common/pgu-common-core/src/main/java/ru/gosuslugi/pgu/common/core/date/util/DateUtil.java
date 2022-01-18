package ru.gosuslugi.pgu.common.core.date.util;

import org.springframework.util.StringUtils;
import ru.gosuslugi.pgu.common.core.date.model.Accuracy;
import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.springframework.util.StringUtils.isEmpty;

public class DateUtil {
    public final static String TODAY_DATE = "today";
    private static final Map<String, Integer> ACCURACY_STRING_CUT = new HashMap<>();
    private static final Map<String, DateTimeFormatter> ACCURACY_FORMATS = new HashMap<>();

    public final static String ESIA_DATE_FORMAT = "dd.MM.yyyy";

    static {
        ACCURACY_STRING_CUT.put(Accuracy.YEAR.getName(), 4);
        ACCURACY_STRING_CUT.put(Accuracy.MONTH.getName(), 7);
        ACCURACY_STRING_CUT.put(Accuracy.DAY.getName(), 10);
        ACCURACY_STRING_CUT.put(Accuracy.HOUR.getName(), 13);
        ACCURACY_STRING_CUT.put(Accuracy.MINUTE.getName(), 16);
        ACCURACY_STRING_CUT.put(Accuracy.SECOND.getName(), 19);

        ACCURACY_FORMATS.put(Accuracy.YEAR.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
        ACCURACY_FORMATS.put(Accuracy.MONTH.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
        ACCURACY_FORMATS.put(Accuracy.DAY.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
        ACCURACY_FORMATS.put(Accuracy.HOUR.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH")
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
        ACCURACY_FORMATS.put(Accuracy.MINUTE.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm")
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
        ACCURACY_FORMATS.put(Accuracy.SECOND.getName(), new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .toFormatter()
                .withZone(ZoneOffset.UTC));
    }

    public static OffsetDateTime parseDate(String value, String accuracy) {
        accuracy = getAccuracy(accuracy);
        value = cutString(value, accuracy);
        try {
            return OffsetDateTime.parse(value, ACCURACY_FORMATS.get(accuracy));
        } catch (DateTimeParseException e) {
            return DateUtil.toOffsetDateTime(value, DateUtil.ESIA_DATE_FORMAT);
        }
    }

    public static String cutString(String value, String accuracy) {
        return value.substring(0, ACCURACY_STRING_CUT.get(accuracy));
    }

    public static String getAccuracy(String accuracy) {
        if (!StringUtils.hasText(accuracy)) {
            accuracy = Accuracy.DAY.getName();
        }
        return accuracy;
    }

    /**
     * EPGUCORE-36558 refactor удалить/вынести, когда будет нормальное решение.
     * @param value значение для форматирования
     * @return если строка не ISO-шная дата, возвращаем то же самое, что и было на входе, в ином случае
     * форматируем в дату
     */
    // TODO поменять в json'ах на рефы вида:
    //  "dateFieldRef": {"path"="dt1.value", "converter": "date", "format": "dd MMMM yyyy г. в HH:mm"}
    //  Затем выпилить это
    public static String checkForDateTime(String value) {
        try {
            LocalDateTime dt = stringToDateTime(value);
            return String.format("%s в %s", ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("ru")).format(dt), dt.format(DateTimeFormatter.ofPattern("HH:mm")));
        } catch (DateTimeParseException ex) {
            // swallow
        }
        return value;
    }

    public static LocalDateTime stringToDateTime(String value) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                // here is the same as your code
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                // optional nanos with 9 digits (including decimal point)
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 9, 9, true)
                .optionalEnd()
                // optional nanos with 6 digits (including decimal point)
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 6, 6, true)
                .optionalEnd()
                // optional nanos with 3 digits (including decimal point)
                .optionalStart()
                .appendFraction(ChronoField.NANO_OF_SECOND, 3, 3, true)
                .optionalEnd()
                // create formatter
                .toFormatter();
        return LocalDateTime.parse(value, formatter);
    }

    public static String checkIfDateIsToday(String dateValue) {
        if (TODAY_DATE.equalsIgnoreCase(dateValue) || !StringUtils.hasText(dateValue)) {
            dateValue = OffsetDateTime.now().toString();
        }
        return dateValue;
    }

    public static OffsetDateTime addToDate(String addToDate, OffsetDateTime value) {
        String addPart = addToDate.replaceAll("(\\w+)=", "\"$1\":");
        HashMap<String, Integer> dateAdder = JsonProcessingUtil.fromJson(addPart, HashMap.class);
        value = value.plusYears(dateAdder.getOrDefault(Accuracy.YEAR.getName(), 0).longValue());
        value = value.plusMonths(dateAdder.getOrDefault(Accuracy.MONTH.getName(), 0).longValue());
        value = value.plusDays(dateAdder.getOrDefault(Accuracy.DAY.getName(), 0).longValue());
        value = value.plusHours(dateAdder.getOrDefault(Accuracy.HOUR.getName(), 0).longValue());
        value = value.plusMinutes(dateAdder.getOrDefault(Accuracy.MINUTE.getName(), 0).longValue());
        value = value.plusSeconds(dateAdder.getOrDefault(Accuracy.SECOND.getName(), 0).longValue());
        return value;
    }

    public static OffsetDateTime toOffsetDateTime(String value, String pattern) {
        return OffsetDateTime.parse(value, new DateTimeFormatterBuilder().appendPattern(pattern)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC));
    }

    public static String toOffsetDateTimeString(String value, String pattern) {
        OffsetDateTime parsed = toOffsetDateTime(value, pattern);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return fmt.format(parsed);
    }

    public static String fromOffsetDateTimeToEsiaFormat(String value) {
        return OffsetDateTime.parse(value).format(DateTimeFormatter.ofPattern(ESIA_DATE_FORMAT));
    }

    public static LocalDate fromEsiaFormat(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(ESIA_DATE_FORMAT));
    }


    public static int convertDateToNumber(YearMonth yearMonth) {
        return yearMonth.getYear() * 12 + yearMonth.getMonthValue();
    }

    public static YearMonth getYearMonthFromMonthNumber(int monthNumber) {
        int year = monthNumber % 12 == 0 ? monthNumber / 12 - 1: monthNumber / 12;
        int month = monthNumber % 12 == 0 ? 12 : monthNumber % 12;
        return YearMonth.of(year, month);
    }

    public static String convertEsiaDateToISODate(String esiaDate) {
        return LocalDate.parse(esiaDate, DateTimeFormatter.ofPattern(ESIA_DATE_FORMAT)).format(DateTimeFormatter.ISO_DATE);
    }

    public static Integer calcAgeFromBirthDate(String birthDate) {
        if (isEmpty(birthDate)) {
            return null;
        }
        OffsetDateTime birthDateOffset = parseDate(birthDate, Accuracy.DAY.getName());
        return (int) YEARS.between(birthDateOffset, OffsetDateTime.now());
    }

    public static Integer calcMonthsForToday(String dateString) {
        if (isEmpty(dateString)) {
            return null;
        }
        OffsetDateTime birthDateOffset = parseDate(dateString, Accuracy.DAY.getName());
        return (int) MONTHS.between(birthDateOffset, OffsetDateTime.now());
    }

}
