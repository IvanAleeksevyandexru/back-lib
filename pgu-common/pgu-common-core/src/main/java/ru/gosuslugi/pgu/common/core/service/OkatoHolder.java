package ru.gosuslugi.pgu.common.core.service;

/**
 * Возвращает ОКАТО
 * @see <a href="https://jira.egovdev.ru/browse/EPGUCORE-52512">EPGUCORE-52512</a>
 */
public interface OkatoHolder {
    String DEFAULT_OKATO = "00000000000";

    /**
     * Возвращает ОКАТО
     * @return ОКАТО
     */
    String getOkato();
}
