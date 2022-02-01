package ru.gosuslugi.pgu.common.core.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Класс для автоматического закрытия произвольного количества ресурсов при использовании try-with-resources
 */
public class CloseableList implements Closeable {

    private final LinkedList<Closeable> resources = new LinkedList<>();

    @Override
    public void close() throws IOException {
        RuntimeException allClosingExceptions = new RuntimeException("Error on closing resource");
        while (!resources.isEmpty()) {
            try {
                resources.removeLast().close();
            } catch (Throwable e) {
                allClosingExceptions.addSuppressed(e);
            }
        }
        if (allClosingExceptions.getSuppressed().length != 0) {
            throw allClosingExceptions;
        }
    }

    public <T extends Closeable> T add(T resource) {
        resources.add(resource);
        return resource;
    }
}
