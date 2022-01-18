package ru.gosuslugi.pgu.common.rendering.render.data;

import org.apache.commons.text.CaseUtils;

/**
 * Поля абстракции адреса.
 */
public enum AddressField {
    CITY,
    CITY_SHORT_TYPE,
    TOWN,
    TOWN_SHORT_TYPE,
    DISTRICT,
    DISTRICT_SHORT_TYPE,
    STREET,
    STREET_SHORT_TYPE,
    REGION,
    REGION_SHORT_TYPE,
    HOUSE,
    HOUSE_SHORT_TYPE,
    BUILDING_1,
    BUILDING_1_SHORT_TYPE,
    BUILDING_2,
    BUILDING_2_SHORT_TYPE,
    APARTMENT,
    APARTMENT_SHORT_TYPE,
    IN_CITY_DIST,
    IN_CITY_DIST_SHORT_TYPE,
    ADDITIONAL_AREA,
    ADDITIONAL_AREA_SHORT_TYPE,
    ADDITIONAL_STREET,
    ADDITIONAL_STREET_SHORT_TYPE;

    private static final char UNDERSCORE_CHAR = '_';

    /**
     * Возвращает название поля адреса в представлении camelCase.
     *
     * @return название поля адреса.
     * @see CaseUtils#toCamelCase(String, boolean, char...)
     */
    public String getName() {
        return CaseUtils.toCamelCase(name(), false, UNDERSCORE_CHAR);
    }
}
