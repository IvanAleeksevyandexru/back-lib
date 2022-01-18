package ru.gosuslugi.pgu.common.esia.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import ru.atc.carcass.security.rest.model.person.Person;
import ru.gosuslugi.pgu.common.esia.search.dto.PersonWithAge;
import ru.gosuslugi.pgu.common.esia.search.dto.PersonWithAgeStub;
import ru.gosuslugi.pgu.common.esia.search.service.PersonSearchService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class PersonSearchServiceStub extends AbstractSearchServiceStub implements PersonSearchService {

    private List<PersonWithAgeStub> personsList;


    @Override
    public Person findUserById(String oid) {
        return personsList.stream().filter(person -> oid.equals(person.getOid())).findFirst().orElse(null);
    }

    @Override
    public List<PersonWithAge> bySnils(String snils) {
        return personsList.stream().filter(person -> snils.equals(person.getSnils())).collect(Collectors.toList());
    }

    @Override
    public List<PersonWithAge> byPassport(String series, String number) {
        return personsList.stream().filter(person -> series.equals(person.getSeries()) && number.equals(person.getNumber())).collect(Collectors.toList());
    }

    public PersonSearchServiceStub(String stubDataFilePath) {
        super(stubDataFilePath);
        try (InputStream targetStream = getSourceFileStream()) {
            personsList = objectMapper.readValue
                    (targetStream,
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            personsList = new ArrayList<>();
            log.error("Error of initialization mock for person search data", e);
        }
    }

    private InputStream getSourceFileStream() throws IOException {
        if (!Optional.ofNullable(stubDataFilePath).orElse("").isEmpty()) {
            return new FileInputStream(stubDataFilePath);
        }
        return getClass().getClassLoader().getResourceAsStream("persons.json");
    }
}
