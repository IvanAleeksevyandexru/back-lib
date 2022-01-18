package ru.gosuslugi.pgu.common.esia.search.service;

import ru.atc.carcass.security.rest.model.person.Person;
import ru.gosuslugi.pgu.common.esia.search.exception.MultiplePersonFoundException;
import ru.gosuslugi.pgu.common.esia.search.dto.PersonWithAge;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;
import static org.springframework.util.CollectionUtils.isEmpty;

public interface PersonSearchService {

    Person findUserById(String oid);

    List<PersonWithAge> bySnils(String snils);

    List<PersonWithAge> byPassport(String series, String number);

    /**
     * Поиск доверенного пользователя из списка
     * @param persons список пользователей
     * @return Первый доверенный пользователь, если такого в списке нет, то первого, иначе null
     */
    private static PersonWithAge chooseTrustedPerson(List<PersonWithAge> persons) {
        if (persons == null) {
            return null;
        }
        List<PersonWithAge> trustedPersons = persons.stream()
                .filter(person -> TRUE.equals(person.getTrusted()))
                .collect(Collectors.toList());
        if (isEmpty(trustedPersons)) {
            return null;
        }
        if (trustedPersons.size() > 1) {
            throw new MultiplePersonFoundException("Найдено несколько пользователей с одинаковыми уникально идентифицирующими данными");
        }
        return trustedPersons.get(0);
    }

    /**
     * Поиск одного доверенного по днным паспорта РФ
     * @param series серия паспорта
     * @param number номер паспорта
     * @return доверенный пользователь ГУ
     */
    default PersonWithAge searchOneTrusted(String series, String number) {
        return chooseTrustedPerson(byPassport(series, number));
    }

    /**
     * Поиск одного доверенного по днным СНИЛС
     * @param snils СНИЛС
     * @return доверенный пользователь ГУ
     */
    default PersonWithAge searchOneTrusted(String snils) {
        return chooseTrustedPerson(bySnils(snils));
    }
}
