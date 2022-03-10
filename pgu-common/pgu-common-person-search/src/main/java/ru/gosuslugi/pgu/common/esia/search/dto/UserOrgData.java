package ru.gosuslugi.pgu.common.esia.search.dto;

import lombok.Data;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.atc.carcass.security.rest.model.EsiaAddress;
import ru.atc.carcass.security.rest.model.EsiaContact;
import ru.atc.carcass.security.rest.model.EsiaVehicle;
import ru.atc.carcass.security.rest.model.orgs.EsiaAttorney;
import ru.atc.carcass.security.rest.model.orgs.Org;
import ru.atc.carcass.security.rest.model.orgs.OrgsContainer;
import ru.atc.carcass.security.rest.model.person.EsiaRole;
import ru.atc.carcass.security.rest.model.person.Person;
import ru.gosuslugi.pgu.common.core.date.util.DateUtil;
import ru.gosuslugi.pgu.common.core.logger.LoggerUtil;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.warn;

/**
 * Хранилище данных юр. лица
 * Заполняется в EsiaAuthFilter данными от ЕСИА
 */
@Component
@Data
@RequestScope
public class UserOrgData {
    public static final String VERIFIED_ATTR = "VERIFIED";
    private static final Logger log = getLogger(UserOrgData.class);

    private Org org;
    private List<EsiaAddress> addresses;
    private List<EsiaContact> contacts;
    private List<EsiaVehicle> vehicles;
    private Person chief;
    private Long chiefUserId;
    private EsiaRole orgRole;
    private String systemAuthority;
    private List<EsiaAttorney> empAttorneys;

    public void update(OrgsContainer orgsContainer, String systemAuthority, List<EsiaAttorney> attorneys) {
        this.setOrg(orgsContainer.getOrg());
        this.setAddresses(orgsContainer.getAddresses());
        this.setContacts(orgsContainer.getContacts());
        this.setVehicles(orgsContainer.getVehicles());
        orgsContainer.getEmployees().stream()
                .filter(Person::isChief)
                .findAny()
                .ifPresent(chiefPerson -> {
                    this.setChief(chiefPerson);
                    this.setChiefUserId(Long.parseLong(chiefPerson.getUserId()));
                });
        orgsContainer.getRoles().stream()
                .filter(role -> (Objects.equals(role.getOid(), org.getOid())))
                .findAny()
                .ifPresent(this::setOrgRole);
        this.setSystemAuthority(systemAuthority);
        this.empAttorneys = attorneys.stream()
                .filter(att -> DateUtil.toOffsetDateTime(att.getExpired(), DateUtil.ESIA_DATE_FORMAT).isAfter(OffsetDateTime.now()))
                .collect(Collectors.toList());
    }

    public boolean hasEmpowerment(Long attCode) {
        return this.empAttorneys.stream().flatMap(att -> att.getEmpowermentList().stream())
                .anyMatch(emp -> emp.getId().equals(attCode));
    }

    /**
     * Возвращает подтветжденное значение контакта или null
     * @param esiaContactTypeCode ESIA код типа контакта
     * @return подтветжденное значение контакта или null
     */
    public String getVerifiedContactValue(String esiaContactTypeCode) {
        return  contacts
                .stream()
                .filter(contact -> Objects.equals(contact.getType(), esiaContactTypeCode) && Objects.equals(contact.getVrfStu(), VERIFIED_ATTR))
                .findFirst()
                .map(EsiaContact::getValue)
                .orElse(null);
    }
}
