package ru.gosuslugi.pgu.draft;

public interface OrderStoreClient {

    /**
     * Получение Xml из хранилища
     * @param orderId - номер заявки
     * @return {@code null}, если XML не найден
     */
    String getOrderXmlById(Long orderId);


    /**
     * Сохранение XML в хранилище
     * @param orderId - номер заявки
     * @param xmlString - сформированный XML
     * @return Черновик
     */
    String saveOrderXml(Long orderId, String xmlString);

    /**
     * Удаление XML из хранилища
     * @param orderId - номер заявки
     */
    void deleteOrderXml(Long orderId);
}
