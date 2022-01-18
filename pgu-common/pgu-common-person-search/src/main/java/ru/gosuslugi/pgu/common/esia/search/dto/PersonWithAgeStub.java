package ru.gosuslugi.pgu.common.esia.search.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PersonWithAgeStub extends PersonWithAge {

    private String series;
    private String number;

}
