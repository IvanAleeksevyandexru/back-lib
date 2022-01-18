package ru.gosuslugi.pgu.draft.client;

import lombok.extern.slf4j.Slf4j;
import ru.gosuslugi.pgu.draft.OrderStoreClient;

@Slf4j
public class OrderStoreClientStub implements OrderStoreClient {

    @Override
    public String getOrderXmlById(Long orderId) {
        log.info("Called stub: OrderStoreClient->getOrderXmlById for orderId: {}", orderId);
        return null;
    }

    @Override
    public String saveOrderXml(Long orderId, String xmlString) {
        log.info("Called stub: OrderStoreClient->saveOrderXml for orderId: {}", orderId);
        return null;
    }

    @Override
    public void deleteOrderXml(Long orderId) {
        log.info("Called stub: OrderStoreClient->deleteOrderXml for orderId: {}", orderId);
    }
}
