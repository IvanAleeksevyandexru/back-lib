package ru.gosuslugi.pgu.common.rendering.render.data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.velocity.context.Context;

/**
 * Запоминает запрошенные placeholder-ы через проксирование {@link Context#get(String)}.
 */
public class UsageAwareContext implements Context {

    /**
     * Специальный префикс, который velocity добавляет для вычитывания значения для нераспознанного
     * placeholder-а. См. {@link org.apache.velocity.runtime.parser.node.ASTReference}, приватный
     * метод {@code getNullString(InternalContextAdapter)}.
     */
    public static final String LITERAL_PREFIX = ".literal.";

    private final Context context;
    private final List<String> usedKeys = new LinkedList<>();

    public UsageAwareContext(Context context) {
        this.context = context;
    }

    @Override
    public Object put(String key, Object value) {
        return this.context.put(key, value);
    }

    @Override
    public Object get(String key) {
        usedKeys.add(key);
        return this.context.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return this.context.containsKey(key);
    }

    @Override
    public Object[] getKeys() {
        return this.context.getKeys();
    }

    @Override
    public Object remove(Object key) {
        return this.context.remove(key);
    }

    public List<String> getUsedKeys() {
        return usedKeys;
    }

    /**
     * Возвращает нераспознанные placeholder-ы: которые начинаются на {@value LITERAL_PREFIX}.
     * <p>
     * Префикс нулевой длины, совпадающий с {@value LITERAL_PREFIX} не учитывается. См.
     * https://jira.egovdev.ru/browse/EPGUCORE-47599.
     *
     * @return нераспознанные placeholder-ы.
     */
    public List<String> getUnrecognizedKeys() {
        return usedKeys.stream()
                .filter(str -> str.startsWith(LITERAL_PREFIX))
                .filter(str -> !LITERAL_PREFIX.equals(str))
                .map(str -> str.substring(LITERAL_PREFIX.length()))
                .collect(Collectors.toList());
    }
}
