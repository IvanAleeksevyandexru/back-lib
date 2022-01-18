package ru.gosuslugi.pgu.dto.descriptor.types;

/**
 * Типы операционных систем для того, чтобы показывать кнопку только в определённой ОС
 */
public enum OperationalSystemType {
    /**
     * для Android-устройств
     */
    Android("Android"),
    /**
     * для iOS-устройств
     */
	iOS("iOS"),
    /**
     * для Harmony-устройств
     */
	Harmony("Harmony"),
    /**
     * для Desktop-устройств
     */
	Desktop("Desktop"),
    /**
     * в случае отличия системы пользователя от прописанных выше
     */
	NotDetermined("NotDetermined"),
    /**
     * в случае ошибки со стороны клиента в момент определения системы
     */
	Error("Error");

    final String type;
    OperationalSystemType(String type) {
        this.type = type;
    }
}
