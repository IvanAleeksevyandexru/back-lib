package ru.gosuslugi.pgu.common.esia.search.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.gosuslugi.pgu.common.esia.search.dto.UserOrgData;
import ru.gosuslugi.pgu.common.esia.search.dto.UserPersonalData;

import java.util.Objects;
import java.util.function.Supplier;

import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.debug;
import static ru.gosuslugi.pgu.common.core.logger.LoggerUtil.warn;

/** Вспомогательный класс, объединяющий логику обработки персональных данных пользователя и юр.лица. */
@UtilityClass
@Slf4j
public class UserDataUtils {

    /**
     * Получение признака руководителя из ФЛ или ЮЛ хранилища данных.
     * @param userPersonalData Хранилище персональных данных пользователя
     * @param userOrgData Хранилище данных юр. лица
     * @return признак руководителя в виде строки {@code true} или {@code false}.
     */
    public String isChief(UserPersonalData userPersonalData, UserOrgData userOrgData) {
        return checkOrgOrPersonal(userPersonalData, userOrgData,
                () -> getOrgChief(userOrgData),
                () -> getChief(userPersonalData)
        );
    }

    /**
     * Проверяет признак chief на null для руководителя или ФЛ.
     * @param userPersonalData Хранилище персональных данных пользователя
     * @param userOrgData Хранилище данных юр. лица
     * @return значение, нужно ли в дальнейшем вычислять chief-признак.
     */
    public boolean nonNullChief(UserPersonalData userPersonalData, UserOrgData userOrgData) {
        return checkOrgOrPersonal(userPersonalData, userOrgData,
                () -> Objects.nonNull(userOrgData.getChief()) && Objects.nonNull(userOrgData.getChief().isChief())
                        || Objects.nonNull(userOrgData.getOrgRole()) && Objects.nonNull(userOrgData.getOrgRole().getChief()),
                () -> Objects.nonNull(userPersonalData.getCurrentRole()) && Objects.nonNull(userPersonalData.getCurrentRole().getChief())
                        || Objects.nonNull(userPersonalData.getPerson()) && Objects.nonNull(userPersonalData.getPerson().isChief())
        );
    }

    /**
     * Определяет, какой из supplier выполнить и вернуть из него результат - для руководителя или ФЛ. Разделение идёт по сравнению userId.
     * @param userPersonalData Хранилище персональных данных пользователя
     * @param userOrgData Хранилище данных юр. лица
     * @param orgSupplier supplier для руководителя
     * @param personalSupplier supplier для ФЛ или сотрудника организации
     * @param <T> результат вычисления выбранного supplier, кладётся в возвращаемое значение
     * @return результат вычисления в одном из supplier
     */
    private <T> T checkOrgOrPersonal(UserPersonalData userPersonalData, UserOrgData userOrgData, Supplier<T> orgSupplier, Supplier<T> personalSupplier) {
        if (userPersonalData.getOrgId() != null) {
            Long userId = userPersonalData.getUserId();
            Long chiefUserId = userOrgData.getChiefUserId();
            if (Objects.equals(userId, chiefUserId)) {
                return orgSupplier.get();
            }
        }
        return personalSupplier.get();
    }

    /**
     * Получение признака руководителя из Person текущего пользователя
     * @param userPersonalData Хранилище персональных данных пользователя.
     * @return Признак руководителя в строке в виде {@code true} или {@code false}.
     */
    // Приходится возвращать строку из-за приведения типа к строке в ConditionCheckerHelper#checkPredicate
    private String getChief(UserPersonalData userPersonalData) {
        if (Objects.nonNull(userPersonalData.getCurrentRole()) && Objects.nonNull(userPersonalData.getCurrentRole().getChief())) {
            debug(log, () -> String.format("Get person chief from currentRole=%s, userId=%s", userPersonalData.getCurrentRole(), userPersonalData.getUserId()));
            return userPersonalData.getCurrentRole().getChief();
        }
        if (Objects.nonNull(userPersonalData.getPerson()) && Objects.nonNull(userPersonalData.getPerson().isChief())) {
            debug(log, () -> String.format("Get person chief=%s, userId=%s",  userPersonalData.getPerson(), userPersonalData.getUserId()));
            return userPersonalData.getPerson().isChief().toString();
        }
        warn(log, () -> String.format("Сhief attribute was null in user person and role with userId=%s", userPersonalData.getUserId()));
        return "false";
    }

    /**
     * Получение признака руководителя из Person руководителя.
     *
     * @param userOrgData Хранилище данных юр. лица
     * @return Признак руководителя в строке в виде {@code true} или {@code false}.
     */
    // Приходится возвращать строку из-за приведения типа к строке в ConditionCheckerHelper#checkPredicate
    private String getOrgChief(UserOrgData userOrgData) {
        if (Objects.nonNull(userOrgData.getChief()) && Objects.nonNull(userOrgData.getChief().isChief())) {
            debug(log, () -> String.format("Get org person chief=%s, oid=%s",  userOrgData.getChief(), userOrgData.getOrg().getOid()));
            return userOrgData.getChief().isChief().toString();
        }
        if (Objects.nonNull(userOrgData.getOrgRole()) && Objects.nonNull(userOrgData.getOrgRole().getChief())) {
            debug(log, () -> String.format("Get org chief from orgRole=%s, oid=%s", userOrgData.getOrgRole(), userOrgData.getOrg().getOid()));
            return userOrgData.getOrgRole().getChief();
        }
        warn(log, () -> String.format("Сhief attribute was null in org person and role with oid=%s", userOrgData.getOrg().getOid()));
        return "false";
    }
}
