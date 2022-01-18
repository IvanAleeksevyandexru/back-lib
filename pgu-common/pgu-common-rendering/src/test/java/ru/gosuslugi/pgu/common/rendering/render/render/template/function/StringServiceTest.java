package ru.gosuslugi.pgu.common.rendering.render.render.template.function;

import static org.testng.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.template.function.StringService;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringServiceTest {
    private static final StringService SUT = new StringService();
    private static final String TEST_OBJECT1_STR = "TestObject1{}";
    private static final String TEST_OBJECT2_STR = "TestObject2{}";
    private static final String DELIM = ", ";
    private static final String EMPTY_STR = "";

    @DataProvider
    public static Object[][] objectsToJoin() {
        return new Object[][]{
                {new Object[]{null}, EMPTY_STR},
                {new Object[]{null, EMPTY_STR}, EMPTY_STR},
                {new Object[]{getObjectWithString(TEST_OBJECT1_STR)}, TEST_OBJECT1_STR},
                {new Object[]{getObjectWithString(TEST_OBJECT1_STR), null}, TEST_OBJECT1_STR},
                {new Object[]{null, getObjectWithString(TEST_OBJECT1_STR)}, TEST_OBJECT1_STR},
                {new Object[]{getObjectWithString(TEST_OBJECT1_STR),
                        getObjectWithString(TEST_OBJECT2_STR)},
                        TEST_OBJECT1_STR.concat(DELIM).concat(TEST_OBJECT2_STR)},
                {new Object[]{getObjectWithString(TEST_OBJECT2_STR), getObjectWithString(EMPTY_STR),
                        getObjectWithString(TEST_OBJECT1_STR)},
                        TEST_OBJECT2_STR.concat(DELIM).concat(TEST_OBJECT1_STR)},
        };
    }

    @DataProvider
    public static Object[][] objectsToJoinUntilBlank() {
        return new Object[][]{
                {new Object[]{null}, EMPTY_STR},
                {new Object[]{null, EMPTY_STR}, EMPTY_STR},
                {new Object[]{getObjectWithString(TEST_OBJECT1_STR)}, TEST_OBJECT1_STR},
                {new Object[]{null, getObjectWithString(TEST_OBJECT1_STR)}, EMPTY_STR},
                {new Object[]{getObjectWithString(TEST_OBJECT1_STR),
                        getObjectWithString(TEST_OBJECT2_STR)},
                        TEST_OBJECT1_STR.concat(DELIM).concat(TEST_OBJECT2_STR)},
                {new Object[]{getObjectWithString(TEST_OBJECT2_STR), getObjectWithString(EMPTY_STR),
                        getObjectWithString(TEST_OBJECT1_STR)},
                        TEST_OBJECT2_STR},
        };
    }

    private static Object getObjectWithString(String str) {
        return new Object() {
            @Override
            public String toString() {
                return str;
            }
        };
    }

    @Test(dataProvider = "objectsToJoin")
    public void shouldConcatProperlyWhenObjectsListGiven(Object[] objects, String expected) {
        // given
        // when
        String actual = SUT.printWithDelimiter(DELIM, objects);

        // then
        assertEquals(actual, expected);
    }

    @Test(dataProvider = "objectsToJoinUntilBlank")
    public void shouldJoinProperlyWhenObjectsListGiven(Object[] objects, String expected) {
        // given
        // when
        String actual = SUT.printLeftJoinWithDelimiter(DELIM, objects);

        // then
        assertEquals(actual, expected);
    }
}
