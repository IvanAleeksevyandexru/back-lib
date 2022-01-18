package ru.gosuslugi.pgu.common.rendering.render.data;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * Запрос на рендеринг документа.
 */
@Data
public class RenderRequest {
    /**
     * Контекст рендеринга.
     * <p>
     * Все будут доступны при обращении из шаблонов.
     */
    private final Map<String, Object> context = new HashMap<>();
    /**
     * Идентификатор услуги.
     * <p>
     * Используется как название архива при скачивании из хранилища описания услуг и как название
     * каталога при получении шаблона из локального хранилища.
     */
    private String serviceId;
    /**
     * Название файла шаблона (может быть указано как с расширением, так и без него).
     */
    private String templateFileName;
}
