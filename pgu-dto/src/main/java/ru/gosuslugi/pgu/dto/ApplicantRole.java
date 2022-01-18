package ru.gosuslugi.pgu.dto;

public enum ApplicantRole {

    /**
     * Основной заявитель, пользователь подавший первое (мастер) заявление
     */
    Applicant,

    /**
     * со-заявитель, который дополняет
     */
    Coapplicant,

    /**
     * пользователь, который даёт согласие, но может чего-то дозаполнить
     */
    Approval,

    /**
     * Родитель ребёнка до 18 лет, который не участвует в заявлении, но даёт согласие на ребёнка
     */
    ApprovalParent,

    /**
     * Ребёнок до 14 лет - он не может зайти в ЛК
     */
    ChildrenUnder14,

    /**
     * Ребёнок от 14 до 18 лет - он полноправный пользователь, но ему нужно согласие родителей при регистрации по месту пребывания
     */
    ChildrenAbove14,

    /**
     * Иные лица при отмене регистрации в "Регистарции ПМП"
     */
    NotParticipant

    }
