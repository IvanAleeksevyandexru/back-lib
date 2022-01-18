package ru.gosuslugi.pgu.dto.esep;

import lombok.Data;

/**
 * Запрос на создания формы подписания заявления ЭЦП
 */
@Data
public class PrepareSignRequest {
    /** Идентификатор заявления */
    private Long orderId;
    /** Идентификатор пользователя */
    private Long userId;
    /** Идентификатор компании пользователя */
    private Long orgId;
    /** URL форм сервиса (нужен для возврата с формы подписания) */
    private String returnUrl;
    /** GUID файлов заявления */
    private String requestGuid;
    /** Признак существования файлов заявления */
    private Boolean filesAlreadyExists;
}