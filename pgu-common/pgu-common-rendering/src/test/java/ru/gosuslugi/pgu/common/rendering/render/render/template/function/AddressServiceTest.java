package ru.gosuslugi.pgu.common.rendering.render.render.template.function;

import static org.testng.Assert.assertEquals;

import ru.gosuslugi.pgu.common.rendering.render.template.function.AddressService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.testng.annotations.Test;

public class AddressServiceTest {
    private static final AddressService SUT = new AddressService();
    private static final String NAME = "Майкоп";
    private static final String SHORT_TYPE = "г";
    private static final String FORMATTED = "Майкоп г.";
    private static final String EMPTY_STRING = "";
    private static final Map<Object, Object> ADDRESS_WITH_SHORTENS;
    private static final Map<Object, Object> ADDRESS_WITH_NO_SHORTENS;
    private static final Map<Object, Object> ADDRESS_WITH_SHORTENS_ONLY;
    private static final Map<Object, Object> ADDRESS_EMPTY = Collections.emptyMap();
    private static final List<Function<Map<Object, Object>, String>> SUT_MAPPINGS =
            Collections.unmodifiableList(Arrays.asList(
                    SUT::city,
                    SUT::town,
                    SUT::district,
                    SUT::street,
                    SUT::region,
                    SUT::house,
                    SUT::building1,
                    SUT::building2,
                    SUT::apartment,
                    SUT::inCityDist,
                    SUT::additionalArea,
                    SUT::additionalStreet));


    static {
        Map<Object, Object> anAddressWithShortens = new HashMap<>();
        anAddressWithShortens.put("city", NAME);
        anAddressWithShortens.put("cityShortType", SHORT_TYPE);
        anAddressWithShortens.put("town", NAME);
        anAddressWithShortens.put("townShortType", SHORT_TYPE);
        anAddressWithShortens.put("district", NAME);
        anAddressWithShortens.put("districtShortType", SHORT_TYPE);
        anAddressWithShortens.put("street", NAME);
        anAddressWithShortens.put("streetShortType", SHORT_TYPE);
        anAddressWithShortens.put("region", NAME);
        anAddressWithShortens.put("regionShortType", SHORT_TYPE);
        anAddressWithShortens.put("house", NAME);
        anAddressWithShortens.put("houseShortType", SHORT_TYPE);
        anAddressWithShortens.put("building1", NAME);
        anAddressWithShortens.put("building1ShortType", SHORT_TYPE);
        anAddressWithShortens.put("building2", NAME);
        anAddressWithShortens.put("building2ShortType", SHORT_TYPE);
        anAddressWithShortens.put("apartment", NAME);
        anAddressWithShortens.put("apartmentShortType", SHORT_TYPE);
        anAddressWithShortens.put("inCityDist", NAME);
        anAddressWithShortens.put("inCityDistShortType", SHORT_TYPE);
        anAddressWithShortens.put("additionalArea", NAME);
        anAddressWithShortens.put("additionalAreaShortType", SHORT_TYPE);
        anAddressWithShortens.put("additionalStreet", NAME);
        anAddressWithShortens.put("additionalStreetShortType", SHORT_TYPE);
        ADDRESS_WITH_SHORTENS = Collections.unmodifiableMap(anAddressWithShortens);

        Map<Object, Object> anAddressWithNoShortens = new HashMap<>();
        anAddressWithNoShortens.put("city", NAME);
        anAddressWithNoShortens.put("town", NAME);
        anAddressWithNoShortens.put("district", NAME);
        anAddressWithNoShortens.put("street", NAME);
        anAddressWithNoShortens.put("region", NAME);
        anAddressWithNoShortens.put("house", NAME);
        anAddressWithNoShortens.put("building1", NAME);
        anAddressWithNoShortens.put("building2", NAME);
        anAddressWithNoShortens.put("apartment", NAME);
        anAddressWithNoShortens.put("inCityDist", NAME);
        anAddressWithNoShortens.put("additionalArea", NAME);
        anAddressWithNoShortens.put("additionalStreet", NAME);
        ADDRESS_WITH_NO_SHORTENS = Collections.unmodifiableMap(anAddressWithNoShortens);

        Map<Object, Object> anAddressWithShortensOnly = new HashMap<>();
        anAddressWithShortensOnly.put("cityShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("townShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("districtShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("streetShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("regionShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("houseShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("building1ShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("building2ShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("apartmentShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("inCityDistShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("additionalAreaShortType", SHORT_TYPE);
        anAddressWithShortensOnly.put("additionalStreetShortType", SHORT_TYPE);
        ADDRESS_WITH_SHORTENS_ONLY = Collections.unmodifiableMap(anAddressWithShortensOnly);
    }

    @Test
    public void shouldProperlyFormatAddressParts() {
        // given
        for (Function<Map<Object, Object>, String> mapping : SUT_MAPPINGS) {
            // when
            String empty = mapping.apply(ADDRESS_EMPTY);
            String nameShorten = mapping.apply(ADDRESS_WITH_SHORTENS);
            String shorten = mapping.apply(ADDRESS_WITH_SHORTENS_ONLY);
            String name = mapping.apply(ADDRESS_WITH_NO_SHORTENS);

            // then
            checkFormatting(empty, nameShorten, shorten, name);
        }

    }

    private void checkFormatting(String empty, String nameShorten, String shorten, String name) {
        assertEquals(empty, EMPTY_STRING);
        assertEquals(nameShorten, FORMATTED);
        assertEquals(shorten, EMPTY_STRING);
        assertEquals(name, NAME);
    }
}
