package ru.gosuslugi.pgu.dto.descriptor.placeholder;

import com.jayway.jsonpath.DocumentContext;
import lombok.Data;

@Data
public abstract class Placeholder {
    /** Ключ для подстановки */
    private String key;

    // Метод переопределен в Reference и не имеет мысла для остальных реализаций.
    public String getPath() {
        return null;
    }

    abstract public PlaceholderType getType();
    abstract public String getNext(DocumentContext... contexts);
}
