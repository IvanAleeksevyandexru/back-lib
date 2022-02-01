package ru.gosuslugi.pgu.common.rendering.render.render.template.function;

import static org.testng.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.template.function.DateService;

import java.text.ParseException;
import java.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DateServiceTest {
    private static final DateService SUT = new DateService();

    @DataProvider
    public static Object[][] defaultFormatCases() {
        return new Object[][]{
                {"2001-01-01T01:50:50.222+07:00", "01.01.2001"},
                {"2001-01-01T01:50:50.222-07:00", "01.01.2001"},
                {"2001-01-01T23:50:50.222+07:00", "01.01.2001"},
                {"2001-01-01T23:50:50.222-07:00", "01.01.2001"},
                {"2001-01-01T23:50:50.222+00:00", "01.01.2001"},
        };
    }

    @DataProvider
    public static Object[][] patternFormatCases() {
        return new Object[][]{
                {"2001-01-01T01:50:50.222+07:00", "yyyy.MM.dd", "2001.01.01"},
                {"2001-01-01T01:50:50.222-07:00", "d", "1"},
                {"2001-01-01T23:50:50.222+07:00", "HH'/'XXX", "23/+07:00"},
        };
    }

    @DataProvider
    public static Object[][] fromIsoCases() {
        return new Object[][]{
                {"2001-01-01T01:50:50+07:00", "01.01.2001"},
                {"2001-01-01T20:50:50-07:00", "01.01.2001"},
                {"2001-01-01T23:50:50Z", "01.01.2001"},
                {"2001-01-01T23:50:50-00:00", "01.01.2001"},
                {"2001-01-01T23:50:01+00:00", "01.01.2001"},
        };
    }

    @DataProvider
    public static Object[][] utcCases() {
        return new Object[][]{
                {"2001-01-01T23:50:50.123Z", "01.01.2001"},
                {"2001-01-01T00:50:50.123Z", "01.01.2001"},
        };
    }


    @DataProvider
    public static Object[][] dateTimeCases() {
        return new Object[][]{
                {"2001-01-01T23:50:50+03:00", "01.01.2001 в 23:50"},
                {"2001-01-01T00:50:50+00:00", "01.01.2001 в 00:50"},
        };
    }

    @DataProvider
    public static Object[][] dateTimeVerbalCases() {
        return new Object[][]{
                {"2001-01-01T23:50:50.000", "Понедельник, 01 января 2001 г. в 23:50"},
                {"2001-01-01T00:50:50.001", "Понедельник, 01 января 2001 г. в 00:50"},
        };
    }

    @DataProvider
    public static Object[][] dayCases() {
        return new Object[][]{
                {"13.11.2010", "13"},
                {"1.11.2005", "01"},
                {"8.1.2005", "08"},
        };
    }

    @DataProvider
    public static Object[][] monthCases() {
        return new Object[][]{
                {"13.1.2010", "января"},
                {"28.02.2005", "февраля"},
                {"8.12.2005", "декабря"},
        };
    }

    @DataProvider
    public static Object[][] yearCases() {
        return new Object[][]{
                {"13.1.2010", "2010"},
                {"28.02.975", "0975"},
        };
    }

    @DataProvider
    public static Object[][] russianFormatCases() {
        return new Object[][]{
                {"2001-01-02", "02.01.2001"},
                {"2001-01", "2001-01"},
        };
    }

    @DataProvider
    public static Object[][] bgaCases() {
        return new Object[][]{
                // время Владивостока должно выводится по Москве
                {"2001-01-01T07:50:50.222+10:00", "01.01.2001"},
                {"2001-01-01T6:50:50.222+10:00", "31.12.2000"},
                // время по Москве должно остаться тем же
                {"2001-01-01T0:50:50.222+03:00", "01.01.2001"},
                {"2001-12-31T23:50:50.222+03:00", "31.12.2001"},
        };
    }

    @DataProvider
    public static Object[][] formatElectronicSignCases() {
        return new Object[][]{
                // время Владивостока должно выводится по Москве
                {"2001-01-01T07:50:50.222+10:00", "01.01.2001 00:50:50"},
                {"2001-01-01T6:50:50.222+10:00", "31.12.2000 23:50:50"},
                // время по Москве должно остаться тем же
                {"2001-01-01T0:50:50.222+03:00", "01.01.2001 00:50:50"},
                {"2001-12-31T23:50:50.222+03:00", "31.12.2001 23:50:50"},
        };
    }

    @DataProvider
    public static Object[][] forRegistrationCases() {
        return new Object[][]{
                {"2001-01-01 01:50:50", "01.01.2001"},
                {"2001-01-1 01:50:50", "01.01.2001"},
                {"2001-01-01 23:5:50", "01.01.2001"},
                {"2001-1-01 3:50:5", "01.01.2001"},
                {"2001-01-01 23:50:50", "01.01.2001"},
        };
    }

    @DataProvider
    public static Object[][] deltaYearCases() {
        Integer currentYear = LocalDate.now().getYear();
        return new Object[][]{
                {String.valueOf(currentYear + 1), -1},
                {String.valueOf(currentYear - 1), 1},
                {String.valueOf(currentYear), 0},
        };
    }

    @Test(dataProvider = "defaultFormatCases")
    public void shouldFormatWithDefaultPatternProperly(String input, String expected) {
        // given
        // when
        String actual = SUT.format(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "patternFormatCases")
    public void shouldFormatWithCustomPatternProperly(String input, String pattern,
                                                      String expected) {
        // given
        // when
        String actual = SUT.format(pattern, input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "fromIsoCases")
    public void shouldFormatFromIsoProperly(String input, String expected) {
        // given
        // when
        String actual = SUT.formatZ(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "utcCases")
    public void shouldFormatFromUtcProperly(String input, String expected) throws ParseException {
        // given
        // when
        String actual = SUT.formatYet(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "dateTimeCases")
    public void shouldFormatDateTimeProperly(String input, String expected) {
        // given
        // when
        String actual = SUT.formatDateAndTime(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "dateTimeVerbalCases")
    public void shouldFormatDateTimeWithVerbalMonthDayProperly(String input, String expected) {
        // given
        // when
        String actual = SUT.formatDateAndTimeWithMonthAndDayNames(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "dayCases")
    public void shouldFormatDayProperly(String input, String expected) throws ParseException {
        // given
        // when
        String actual = SUT.getDay(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "monthCases")
    public void shouldFormatMonthProperly(String input, String expected) throws ParseException {
        // given
        // when
        String actual = SUT.getMonth(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "yearCases")
    public void shouldFormatYearProperly(String input, String expected) throws ParseException {
        // given
        // when
        String actual = SUT.getYear(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "russianFormatCases")
    public void shouldFormatToRussianProperly(String input, String expected) {
        // given
        // when
        String actual = SUT.convertToRussianDate(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "bgaCases")
    public void shouldFormatBgaProperly(String input, String expected) throws ParseException {
        // given
        // when
        String actual = SUT.formatBGA(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "formatElectronicSignCases")
    public void shouldFormatElectronicSignProperly(String input, String expected)
            throws ParseException {
        // given
        // when
        String actual = SUT.formatForElectronicSigns(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "forRegistrationCases")
    public void shouldFormatForRegistrationProperly(String input, String expected)
            throws ParseException {
        // given
        // when
        String actual = SUT.formatForRegistrationDate(input);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "deltaYearCases")
    public void shouldProperlySubtractYears(String input, Integer expected) {
        // given
        // when
        Integer actual = SUT.getDeltaYears(input);

        // then
        assertEquals(actual, expected);
    }
}
