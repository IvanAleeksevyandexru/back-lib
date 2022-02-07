package ru.gosuslugi.pgu.sd.storage;

import ru.gosuslugi.pgu.dto.descriptor.ServiceDescriptor;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

/**
 * Клиент хранилища дескрипторов услуг.
 * Кеширует услуги в локальном кэше, обновляя их раз в 10 минут из базы.
 */
public interface ServiceDescriptorClient {

    /**
     * Получение сервис-дескриптора в виде строки
     * @param serviceId - идентификатор сервис-дескриптора
     * @return ServiceDescriptor
     */
    ServiceDescriptor getServiceDescriptor(String serviceId);

    /**
     * Сохранение сервис-дескриптора
     * @param serviceId - идентификатор сервис-дескриптора
     * @param serviceDescriptor - сервис-дескриптор
     */
    void saveServiceDescriptor(String serviceId, ServiceDescriptor serviceDescriptor);

    /**
     * Удаляет из кэша сценарий
     * @param serviceId идентификатор сервис-дескриптора
     */
    void clearCachedDescriptor(String serviceId);

    /**
     * Получение архива с Velocity-шаблонами для сервиса
     * @param serviceId - идентификатор сервиса
     * @return строка
     */
    ByteBuffer getTemplatePackage(String serviceId);

    /**
     * Получение штампа-времени обновления Velocity-шаблонов сервиса в хранилище
     * @param serviceId - идентификатор сервиса
     * @return штамп-времени
     */
    String getTemplateRefreshTime(String serviceId);

    Map<String, String> getTemplatePackageChecksums(Collection<String> serviceId);
}
