package ru.gosuslugi.pgu.common.esia.search.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.atc.carcass.security.rest.model.person.Person;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@EqualsAndHashCode(callSuper=false)
public class PersonWithAge extends Person {

    public static final DateTimeFormatter  BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private LocalDate date;

    private String oid;

    private boolean exists;

    public Integer getAge() {
        if (isEmpty(getBirthDate())) {
            return null;
        }
        if (date == null) {
            date = LocalDate.parse(getBirthDate(), BIRTH_DATE_FORMATTER);
        }

        return Period.between(date, LocalDate.now()).getYears();
    }

}
