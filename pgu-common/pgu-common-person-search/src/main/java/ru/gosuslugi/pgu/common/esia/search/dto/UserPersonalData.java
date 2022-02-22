package ru.gosuslugi.pgu.common.esia.search.dto;

import lombok.Data;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.atc.carcass.security.model.EsiaOAuthTokenSession;
import ru.atc.carcass.security.rest.model.EsiaAddress;
import ru.atc.carcass.security.rest.model.EsiaContact;
import ru.atc.carcass.security.rest.model.person.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Хранилище персональных данных пользователя.
 * Заполняется в EsiaAuthFilter данными от ЕСИА
 */
@Data
@Component
@RequestScope
public class UserPersonalData {

    private static final Predicate<PersonDoc> isOmsDocType = x -> ("MDCL_PLCY".equals(x.getType()));
    private static final Pattern ALL_WHITESPACES = Pattern.compile("(\\s*-\\s*)|(\\s+)");

    private String token;

    private Long userId;

    private Long orgId;

    private Person person;

    private List<Kids> kids;

    private List<EsiaAddress> addresses;

    private List<PersonDoc> docs;

    private List<EsiaContact> contacts;

    private String applicantType;

    private EsiaRole currentRole;

    private String authorityId;

    public void update(PersonContainer personContainer, EsiaOAuthTokenSession tokenInfo) {
        this.setToken(tokenInfo.getSessionToken());
        this.setAuthorityId(tokenInfo.getAuthorityId());
        this.setUserId(Long.parseLong(tokenInfo.getUserId()));
        this.setOrgId(isEmpty(tokenInfo.getOrgOid()) ? null : Long.parseLong(tokenInfo.getOrgOid()));
        this.setPerson(personContainer.getPerson());
        this.setKids(personContainer.getKids());
        this.setAddresses(personContainer.getAddresses());
        this.setDocs(personContainer.getDocs());
        this.setContacts(personContainer.getContacts());
        if (this.getOrgId() != null) {
            String orgId = Long.toString(this.getOrgId());
            this.setCurrentRole(
                    personContainer
                            .getRoles()
                            .stream()
                            .filter(role-> orgId.equals(role.getOid()))
                            .findFirst()
                            .orElse(null)
            );
        }
    }

    public void fillKidsOms() {
        for (Kids kid : kids) {
            Optional<PersonDoc> kidOms = kid.getDocuments().getDocs().stream()
                    .filter(isOmsDocType)
                    .findFirst();
            if (kidOms.isPresent()) {
                Optional<PersonDoc> omsBox = buildOmsDocument(kidOms.get());
                omsBox.ifPresent(oms -> {
                    kidOms.get().setNumber(oms.getNumber());
                    kidOms.get().setSeries(oms.getSeries());
                });
            }
        }
    }

    public Optional<PersonDoc> getOmsDocument() {
        Optional<PersonDoc> omsbox = docs.stream()
                .filter(isOmsDocType)
                .findFirst();
        if (omsbox.isPresent())
            return buildOmsDocument(omsbox.get());
        return Optional.empty();
    }

    private static Optional<PersonDoc> buildOmsDocument(PersonDoc oms) {
        if (Objects.isNull(oms) || Objects.isNull(oms.getNumber())) {
            return Optional.empty();
        }
        PersonDoc omsDoc = new PersonDoc();
        omsDoc.setUnitedNumber(oms.getUnitedNumber());
        omsDoc.setIssuePlace(oms.getIssuePlace());
        omsDoc.setMedicalOrg(oms.getMedicalOrg());
        omsDoc.setIssueId(oms.getIssueId());
        omsDoc.setIssuedBy(oms.getIssuedBy());
        String series = Objects.nonNull(oms.getSeries()) ? oms.getSeries() : "";
        String number = oms.getNumber();
        omsDoc.setNumber(number);
        omsDoc.setSeries(series);
        if (!series.equals("")) {
            return Optional.of(omsDoc);
        }
        number = ALL_WHITESPACES.matcher(number).replaceAll("/");// для удобства свожу все разделители к одному
        String[] splitted = number.contains("№") ? number.split( "№") : number.split("/");

        if (splitted.length == 1 || splitted.length == 4) {
            String replacement = number.replace("/", "");
            if (replacement.length() == 16 && NumberUtils.isDigits(replacement)) {
                omsDoc.setNumber(replacement);
                return Optional.of(omsDoc);
            }
        }

        if (splitted.length == 2) {
            String replacement = splitted[1].replace("/", "");
            if (replacement.length() >= 1 && NumberUtils.isDigits(replacement)) {
                omsDoc.setSeries(splitted[0].replace("/", ""));
                omsDoc.setNumber(replacement);
                return Optional.of(omsDoc);
            }
        }
        return Optional.of(omsDoc);
    }

    public String getVerifiedPhoneNumber(){
        return Optional.ofNullable(contacts)
                .flatMap(list -> list.stream()
                        .filter(c ->
                                (Objects.equals(c.getType(), EsiaContact.Type.MOBILE_PHONE.getCode()))
                                        && Objects.equals(c.getVrfStu(), "VERIFIED"))
                        .findAny())
                .map(EsiaContact::getValue).orElse(null);
    }

    public Long generateUniqueUserValue() {
        Long min = 1000000000L;
        Long max = 9999999999L;
        byte[] arr = new byte[1];
        arr[0] = Long.valueOf(userId).byteValue();
        Random random = new SecureRandom(arr);
        return random.longs(1, min, max).findFirst().getAsLong();
    }

    public Object getChief() {
        if (Objects.nonNull(currentRole) && Objects.nonNull(currentRole.getChief())) {
            return currentRole.getChief();
        }
        return Objects.nonNull(person) ? person.isChief() : null;
    }
}
