package ru.gosuslugi.pgu.common.rendering.render.render.data;

import static org.testng.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.data.AddressField;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddressFieldTest {
    @DataProvider
    public static Object[][] fields() {
        return new Object[][]{
                {AddressField.CITY_SHORT_TYPE, "cityShortType"},
                {AddressField.TOWN, "town"},
                {AddressField.BUILDING_1, "building1"},
                {AddressField.BUILDING_2_SHORT_TYPE, "building2ShortType"},
                {AddressField.IN_CITY_DIST_SHORT_TYPE, "inCityDistShortType"},
        };
    }

    @Test(dataProvider = "fields")
    public void shouldProperlyConvertToFieldKey(AddressField field, String expected) {
        // given
        // when
        String actual = field.getName();

        // then
        assertEquals(actual, expected);
    }
}
