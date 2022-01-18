package ru.gosuslugi.pgu.common.core.service;


import ru.gosuslugi.pgu.common.core.service.dto.DictionayHealthDto;
import ru.gosuslugi.pgu.common.core.service.dto.HealthDto;

public interface HealthHolder {

    void addDictionaryHealth(DictionayHealthDto health);

    HealthDto get();
}
