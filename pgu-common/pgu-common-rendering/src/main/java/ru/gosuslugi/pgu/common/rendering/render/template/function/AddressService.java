package ru.gosuslugi.pgu.common.rendering.render.template.function;

import ru.gosuslugi.pgu.common.rendering.render.data.AddressField;

import java.util.Map;
import java.util.Optional;

public class AddressService {
    private static final StringService STRING_SERVICE = new StringService();
    private static final String POINT = ".";
    private static final String EMPTY_STR = "";
    private static final String WS = " ";

    public String cityAndTown(Map<Object, Object> address) {
        return STRING_SERVICE.printWithDelimiter(
                ", ",
                city(address),
                town(address)
        );
    }

    public String city(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.CITY),
                withPoint(address, AddressField.CITY_SHORT_TYPE)
        );
    }

    public String town(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.TOWN),
                withPoint(address, AddressField.TOWN_SHORT_TYPE)
        );
    }

    public String district(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.DISTRICT),
                withPoint(address, AddressField.DISTRICT_SHORT_TYPE)
        );
    }

    public String street(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.STREET),
                withPoint(address, AddressField.STREET_SHORT_TYPE)
        );
    }

    public String region(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.REGION),
                withPoint(address, AddressField.REGION_SHORT_TYPE)
        );
    }

    public String house(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.HOUSE),
                withPoint(address, AddressField.HOUSE_SHORT_TYPE)
        );
    }

    public String building1AndBuilding2(Map<Object, Object> address) {
        return STRING_SERVICE.printWithDelimiter(
                ", ",
                building1(address),
                building2(address)
        );
    }

    public String building1(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.BUILDING_1),
                withPoint(address, AddressField.BUILDING_1_SHORT_TYPE)
        );
    }

    public String building2(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.BUILDING_2),
                withPoint(address, AddressField.BUILDING_2_SHORT_TYPE)
        );
    }

    public String apartment(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.APARTMENT),
                withPoint(address, AddressField.APARTMENT_SHORT_TYPE)
        );
    }

    public String inCityDist(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.IN_CITY_DIST),
                withPoint(address, AddressField.IN_CITY_DIST_SHORT_TYPE)
        );
    }

    public String additionalArea(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.ADDITIONAL_AREA),
                withPoint(address, AddressField.ADDITIONAL_AREA_SHORT_TYPE)
        );
    }

    public String additionalStreet(Map<Object, Object> address) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(WS,
                getMapSafety(address, AddressField.ADDITIONAL_STREET),
                withPoint(address, AddressField.ADDITIONAL_STREET_SHORT_TYPE)
        );
    }

    private Object getMapSafety(Map<Object, Object> address, AddressField key) {
        return Optional.ofNullable(address).map(map -> map.get(key.getName())).orElse(null);
    }

    private String withPoint(Map<Object, Object> address, final AddressField field) {
        return STRING_SERVICE.printLeftJoinWithDelimiter(EMPTY_STR, getMapSafety(address, field),
                POINT);
    }
}
