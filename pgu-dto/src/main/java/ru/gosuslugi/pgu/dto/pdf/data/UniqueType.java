package ru.gosuslugi.pgu.dto.pdf.data;

public enum UniqueType {

    /** Не использовать уникальное значение */
    NONE,

    /** Уникальное значение высчитываемое на основании hash */
    HASH,

    /** Уникальное значение на основании GUID */
    GUID
}
