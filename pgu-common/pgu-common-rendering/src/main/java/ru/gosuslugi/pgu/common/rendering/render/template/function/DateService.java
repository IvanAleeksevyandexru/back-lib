package ru.gosuslugi.pgu.common.rendering.render.template.function;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Конвертирует форматы представления дат.
 */
@Slf4j
public class DateService {
    private static final String INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern(
            INPUT_FORMAT);
    private static final TimeZone MSK_TIME_ZONE = TimeZone.getTimeZone("Europe/Moscow");
    private static final String DATE_DOT_FORMAT = "dd.MM.yyyy";
    public static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern(
            DATE_DOT_FORMAT);
    private static final SimpleDateFormat DATE_DOT_FORMATTER = new SimpleDateFormat(
            DATE_DOT_FORMAT);
    private static final SimpleDateFormat DATE_DOT_AT_MSK_FORMATTER = new SimpleDateFormat(
            DATE_DOT_FORMAT);
    private static final String PATTERN_WITHOUT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final DateTimeFormatter FORMAT_WITHOUT_TIMEZONE = DateTimeFormatter.ofPattern(
            PATTERN_WITHOUT_TIMEZONE);
    private static final String OUTPUT_FORMAT_WITH_TIME = "dd.MM.yyyy 'в' HH:mm";
    public static final DateTimeFormatter OUTPUT_FORMATTER_WITH_TIME =
            DateTimeFormatter.ofPattern(OUTPUT_FORMAT_WITH_TIME);
    private static final String OUTPUT_FORMAT_WITH_TIME_DAY_AND_MONTH = "EEEE, dd MMMM yyyy 'г. в' HH:mm";
    public static final DateTimeFormatter OUTPUT_FORMATTER_WITH_TIME_DAY_AND_MONTH =
            DateTimeFormatter.ofPattern(OUTPUT_FORMAT_WITH_TIME_DAY_AND_MONTH, new Locale("ru"));
    private static final Map<String, String> MONTH_MAP = new HashMap<>();
    private static final String INPUT_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final SimpleDateFormat INPUT_UTC_FORMATTER = new SimpleDateFormat(
            INPUT_UTC_FORMAT);
    private static final SimpleDateFormat DAY_LZ_FORMATTER = new SimpleDateFormat("dd");
    private static final SimpleDateFormat MONTH_LZ_FORMATTER = new SimpleDateFormat("MM");
    private static final SimpleDateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");
    private static final String DATE_HYPHEN_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat DATE_HYPHEN_FORMATTER = new SimpleDateFormat(
            DATE_HYPHEN_FORMAT);
    private static final String DATE_TIME_MS_NLZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SXXX";
    private static final SimpleDateFormat DATE_TIME_MS_NLZ_FORMATTER = new SimpleDateFormat(
            DATE_TIME_MS_NLZ_FORMAT);
    private static final String DATE_DOT_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private static final SimpleDateFormat DATE_DOT_TIME_AT_MSK_FORMATTER = new SimpleDateFormat(
            DATE_DOT_TIME_FORMAT);
    private static final String DATE_HYPHEN_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_HYPHEN_TIME_FORMATTER = new SimpleDateFormat(
            DATE_HYPHEN_TIME_FORMAT);

    static {
        MONTH_MAP.put("01", "января");
        MONTH_MAP.put("02", "февраля");
        MONTH_MAP.put("03", "марта");
        MONTH_MAP.put("04", "апреля");
        MONTH_MAP.put("05", "мая");
        MONTH_MAP.put("06", "июня");
        MONTH_MAP.put("07", "июля");
        MONTH_MAP.put("08", "августа");
        MONTH_MAP.put("09", "сентября");
        MONTH_MAP.put("10", "октября");
        MONTH_MAP.put("11", "ноября");
        MONTH_MAP.put("12", "декабря");

        DATE_DOT_TIME_AT_MSK_FORMATTER.setTimeZone(MSK_TIME_ZONE);
        DATE_DOT_AT_MSK_FORMATTER.setTimeZone(MSK_TIME_ZONE);
    }

    /**
     * Преобразует date из входного формата {@value INPUT_FORMAT} в выходной {@value
     * DATE_DOT_FORMAT}.
     *
     * @param date дата.
     * @return форматированная дата.
     */
    public String format(String date) {
        return OUTPUT_FORMATTER.format(ZonedDateTime.parse(date, INPUT_FORMATTER));
    }

    /**
     * Преобразует date из входного формата {@value INPUT_FORMAT} в выходной template.
     *
     * @param template шаблон выходного формата.
     * @param date дата.
     * @return форматированная дата.
     */
    public String format(String template, String date) {
        return DateTimeFormatter.ofPattern(template)
                .format(ZonedDateTime.parse(date, INPUT_FORMATTER));
    }

    /**
     * Форматирует date из формата {@link DateTimeFormatter#ISO_DATE_TIME} в выходной {@value
     * DATE_DOT_FORMAT}.
     *
     * @param date дата.
     * @return форматированная дата.
     */
    public String formatZ(String date) {
        return OUTPUT_FORMATTER.format(ZonedDateTime.parse(date, ISO_DATE_TIME));
    }


    /**
     * Форматирует date из формата {@value INPUT_UTC_FORMAT} в выходной {@value DATE_DOT_FORMAT}.
     *
     * @param date дата.
     * @return форматированная дата.
     * @throws ParseException, если date была представлена в неверном формате.
     * @see SimpleDateFormat
     */
    public String formatYet(String date) throws ParseException {
        Date dt = INPUT_UTC_FORMATTER.parse(date);
        return DATE_DOT_FORMATTER.format(dt);
    }

    /**
     * Форматирует date из формата {@link DateTimeFormatter#ISO_DATE_TIME} в выходной {@value
     * OUTPUT_FORMAT_WITH_TIME}.
     * <p>
     * Пример форматированной даты: {@code 01.01.2001 в 00:50}.
     *
     * @param date дата.
     * @return форматированная дата.
     */
    public String formatDateAndTime(String date) {
        return OUTPUT_FORMATTER_WITH_TIME.format(ZonedDateTime.parse(date, ISO_DATE_TIME));
    }

    /**
     * Форматирует date из формата {@value PATTERN_WITHOUT_TIMEZONE} в выходной {@value
     * OUTPUT_FORMAT_WITH_TIME_DAY_AND_MONTH}.
     * <p>
     * Месяц и день представлены в русскоязычной словесной форме. Первая буква -- заглавная. Пример
     * форматированной даты: {@code Понедельник, 01 января 2001 г. в 00:50}.
     *
     * @param date дата.
     * @return форматированная дата.
     */
    public String formatDateAndTimeWithMonthAndDayNames(String date) {
        return StringUtils.capitalize(OUTPUT_FORMATTER_WITH_TIME_DAY_AND_MONTH.format(
                LocalDateTime.parse(date, FORMAT_WITHOUT_TIMEZONE)));
    }

    /**
     * Извлекает из date из формата {@value DATE_DOT_FORMAT} день и выводит его с ведущим нулем.
     * <p>
     * Например, {@code 1.2.2005} &#8594; {@code 01}.
     *
     * @param date дата.
     * @return день.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String getDay(String date) throws ParseException {
        Date dt = DATE_DOT_FORMATTER.parse(date);
        return DAY_LZ_FORMATTER.format(dt);
    }

    /**
     * Извлекает из date из формата {@value DATE_DOT_FORMAT} месяц и выводит его в вербальной форме
     * на русском языке в родительном падеже.
     * <p>
     * Например, {@code 1.2.2005} &#8594; {@code февраля}.
     *
     * @param date дата.
     * @return месяц.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String getMonth(String date) throws ParseException {
        Date dt = DATE_DOT_FORMATTER.parse(date);
        String monthNum = MONTH_LZ_FORMATTER.format(dt);
        return MONTH_MAP.get(monthNum);
    }

    /**
     * Извлекает из date из формата {@value DATE_DOT_FORMAT} год и выводит его 4-мя цифрами с
     * ведущими нулями.
     * <p>
     * Например, {@code 1.2.975} &#8594; {@code 0975}.
     *
     * @param date дата.
     * @return год.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String getYear(String date) throws ParseException {
        Date dt = DATE_DOT_FORMATTER.parse(date);
        return YEAR_FORMATTER.format(dt);
    }

    /**
     * Форматирует date из формата {@value DATE_HYPHEN_FORMAT} в выходной {@value DATE_DOT_FORMAT}.
     * <p>
     * Например, {@code 2001-01-02} &#8594; {@code 02.01.2001}.
     * <p>
     * Если формат date не соответствует ожидаемому, то в лог выводится предупреждение. Исключение
     * не выбрасывается.
     *
     * @param date дата.
     * @return форматированная дата.
     */
    public String convertToRussianDate(String date) {
        try {
            Date dt = DATE_HYPHEN_FORMATTER.parse(date);
            return DATE_DOT_FORMATTER.format(dt);
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Не удалось распарсить дату '" + date + "'. Ожидаемый формат: "
                        + DATE_HYPHEN_FORMAT, e);
            }
            return date;
        }
    }

    /**
     * Форматирует date из формата {@value DATE_TIME_MS_NLZ_FORMAT} в выходной {@value
     * DATE_DOT_FORMAT}.
     * <p>
     * Внимание! Часовой пояс date будет приведен к Москве. Т. е. если часовой пояс на входе был +7,
     * то ввиду того, что часовой пояс Москвы +3, из исходного времени при выводе будет вычитаться
     * разница — 4 часа.
     * <p>
     * Например, {@code 2001-01-01T01:50:50.222+07:00} &#8594; {@code 31.12.2000}.
     *
     * @param date дата с часовым поясом.
     * @return форматированная дата по Москве.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String formatBGA(String date) throws ParseException {
        Date dt = DATE_TIME_MS_NLZ_FORMATTER.parse(date);
        return DATE_DOT_AT_MSK_FORMATTER.format(dt);
    }

    /**
     * Преобразует даты в формат, используемый в электронных подписях.
     * <p>
     * Форматирует date из формата {@value DATE_TIME_MS_NLZ_FORMAT} в выходной {@value
     * DATE_DOT_TIME_FORMAT}.
     * <p>
     * Внимание! Часовой пояс date будет приведен к Москве. Т. е. если часовой пояс на входе был +7,
     * то ввиду того, что часовой пояс Москвы +3, из исходного времени при выводе будет вычитаться
     * разница — 4 часа.
     * <p>
     * Например, {@code 2001-01-01T23:50:50.0-07:00} &#8594; {@code 02.01.2001 09:50:50}.
     *
     * @param date дата с часовым поясом.
     * @return форматированная дата по Москве.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String formatForElectronicSigns(String date) throws ParseException {
        Date dt = DATE_TIME_MS_NLZ_FORMATTER.parse(date);
        return DATE_DOT_TIME_AT_MSK_FORMATTER.format(dt);
    }

    /**
     * Форматирует date из формата {@value DATE_HYPHEN_TIME_FORMAT} в выходной {@value
     * DATE_DOT_FORMAT}.
     * <p>
     * Например, {@code 2001-1-01 3:50:5} &#8594; {@code 01.01.2001}.
     *
     * @param date дата.
     * @return форматированная дата.
     * @throws ParseException если date представлена в неверном формате.
     */
    public String formatForRegistrationDate(String date) throws ParseException {
        Date dt = DATE_HYPHEN_TIME_FORMATTER.parse(date);
        return new SimpleDateFormat(DATE_DOT_FORMAT).format(dt);
    }

    /**
     * Возвращает разницу текущий год минус year.
     *
     * @param year вычитаемый год.
     * @return разница между текущим годом и year.
     */
    public Integer getDeltaYears(String year) {
        return LocalDate.now().getYear() - Integer.parseInt(year);
    }

}
