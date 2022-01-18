package ru.gosuslugi.pgu.common.rendering.render.render.template.function;

import static org.junit.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.template.function.MoneyService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MoneyServiceTest {
    private static final MoneyService SUT = new MoneyService();
    private static final String RUB_PLURAL_GENITIVE = "рублей";
    private static final String RUB_SINGULAR_NOMINATIVE = "рубль";
    private static final String RUB_SINGULAR_GENITIVE = "рубля";
    private static final String OWNER_SINGULAR_NOMINATIVE = "владелец";
    private static final String OWNER_SINGULAR_GENITIVE = "владельца";
    private static final String OWNER_PLURAL_GENITIVE = "владельцев";
    private static final List<String> OWNER_CASES =
            Stream.of(OWNER_SINGULAR_NOMINATIVE, OWNER_SINGULAR_GENITIVE, OWNER_PLURAL_GENITIVE)
                    .collect(Collectors.toUnmodifiableList());

    @DataProvider
    public static Object[][] rubDigitToVerbalCases() {
        return new Object[][]{
                {0, RUB_PLURAL_GENITIVE},
                {1, RUB_SINGULAR_NOMINATIVE},
                {2, RUB_SINGULAR_GENITIVE},
                {4, RUB_SINGULAR_GENITIVE},
                {5, RUB_PLURAL_GENITIVE},
                {10, RUB_PLURAL_GENITIVE},
                {20, RUB_PLURAL_GENITIVE},
                {21, RUB_SINGULAR_NOMINATIVE},
                {23, RUB_SINGULAR_GENITIVE},
                {25, RUB_PLURAL_GENITIVE},
        };
    }

    @DataProvider
    public static Object[][] ownerCases() {
        return new Object[][]{
                {0, OWNER_PLURAL_GENITIVE},
                {1, OWNER_SINGULAR_NOMINATIVE},
                {2, OWNER_SINGULAR_GENITIVE},
                {4, OWNER_SINGULAR_GENITIVE},
                {5, OWNER_PLURAL_GENITIVE},
                {10, OWNER_PLURAL_GENITIVE},
                {20, OWNER_PLURAL_GENITIVE},
                {21, OWNER_SINGULAR_NOMINATIVE},
                {23, OWNER_SINGULAR_GENITIVE},
                {25, OWNER_PLURAL_GENITIVE},
        };
    }

    @Test
    public void shouldConvertToVerbalRubDigitKopWhenFloatGiven() {
        // given
        final String floatFormatted = "234 ,4345 ₽";

        // when
        final String actual = SUT.printWordRubDigitKop(floatFormatted);

        // then
        assertEquals("двести тридцать четыре руб. 43 коп.", actual);
    }

    @Test(dataProvider = "rubDigitToVerbalCases")
    public void shouldProperlyInclineRubleWhenDigitGiven(Integer digit, String expected) {
        // given
        // when
        final String actual = SUT.getRubWord(digit);

        // then
        assertEquals(expected, actual);
    }

    @Test(dataProvider = "ownerCases")
    public void shouldProperlyInclineOwnerWhenDigitGiven(Integer digit, String expected) {
        // given
        // when
        final String actual = SUT.getDeclensionOfWords(digit, OWNER_CASES);

        // then
        assertEquals(expected, actual);
    }
}
