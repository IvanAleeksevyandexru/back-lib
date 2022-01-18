package ru.gosuslugi.pgu.common.core.service.impl;

import lombok.AllArgsConstructor;
import ru.gosuslugi.pgu.common.core.service.HealthHolder;
import ru.gosuslugi.pgu.common.core.service.dto.DictionayHealthDto;
import ru.gosuslugi.pgu.common.core.service.dto.HealthDto;

@AllArgsConstructor
public class HealthHolderImpl implements HealthHolder {
    private final HealthDto healthDto = new HealthDto();

    public void addDictionaryHealth(DictionayHealthDto health) {
        healthDto.getDictionaries().add(health);
    }

    public HealthDto get() {
        return healthDto;
    }
}
