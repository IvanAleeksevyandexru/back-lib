package ru.gosuslugi.pgu.common.rendering.template.service;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Service for getting template by service ID
 */
public interface TemplateService {

    /**
     * Getting lock with serviceId and template file name. Side effect is downloading of template to
     * the local file cache.
     *
     * @param serviceId service ID
     * @return lock for concurrent work with the template
     */
    ReentrantReadWriteLock getOrDownloadAndLock(String serviceId);

}
