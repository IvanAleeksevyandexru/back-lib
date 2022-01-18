package ru.gosuslugi.pgu.dto;

import lombok.Data;

@Data
public class Address {
    private String region;
    private String city;
    private String district;
    private String town;
    private String inCityDist;
    private String street;
    private String additionalArea;
    private String additionalStreet;
    private String house;
    private boolean houseCheckbox;
    private boolean houseCheckboxClosed;
    private String building1;
    private String building2;
    private String apartment;
    private boolean apartmentCheckbox;
    private boolean apartmentCheckboxClosed;
    private String index;
    private String fullAddress;
    private String addressStr;
    private String lat;
    private String lng;
    private String fiasCode;
    private int hasErrors;
}
