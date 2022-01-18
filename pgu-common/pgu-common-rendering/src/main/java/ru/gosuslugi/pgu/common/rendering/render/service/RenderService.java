package ru.gosuslugi.pgu.common.rendering.render.service;

import ru.gosuslugi.pgu.common.rendering.render.data.RenderRequest;
import ru.gosuslugi.pgu.common.rendering.render.exception.RenderTemplateException;

/**
 * Формирует XML-файл на основе шаблона.
 */
public interface RenderService {

    /**
     * Формирует XML-файлы на основе шаблона согласно параметрам запроса.
     *
     * @param request запрос на формирование документа.
     * @return содержимое документа.
     * @throws RenderTemplateException если шаблон составлен неверно или остались неразрешенные
     * переменные.
     * @throws ru.gosuslugi.pgu.common.rendering.render.exception.TemplateNotFoundException если в
     * локальном хранилище не найден файл шаблона.
     */
    String render(RenderRequest request);
}
