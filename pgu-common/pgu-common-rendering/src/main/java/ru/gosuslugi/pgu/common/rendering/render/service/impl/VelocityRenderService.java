package ru.gosuslugi.pgu.common.rendering.render.service.impl;

import ru.gosuslugi.pgu.common.core.json.JsonProcessingUtil;
import ru.gosuslugi.pgu.common.rendering.render.config.VelocityConfig;
import ru.gosuslugi.pgu.common.rendering.render.data.RenderRequest;
import ru.gosuslugi.pgu.common.rendering.render.data.UsageAwareContext;
import ru.gosuslugi.pgu.common.rendering.render.exception.RenderTemplateException;
import ru.gosuslugi.pgu.common.rendering.render.exception.TemplateNotFoundException;
import ru.gosuslugi.pgu.common.rendering.render.service.RenderService;
import ru.gosuslugi.pgu.common.rendering.template.service.TemplateService;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Реализует формирование XML-файлов, посредством Apache Velocity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VelocityRenderService implements RenderService {
    private static final String TEMPLATE_EXTENSION = ".vm";
    /**
     * Шаблон контекста.
     *
     * @see VelocityConfig#prototypeTemplateContext()
     */
    private final VelocityContext contextPrototype;
    /**
     * Движок Velocity.
     */
    private final VelocityEngine engine;
    private final TemplateService templateService;
    @Value("${service-descriptor-storage-client.integration:#{false}}")
    private boolean sdIntegrationEnabled;

    @Override
    public String render(RenderRequest request) {
        VelocityContext context = prepareTemplateContext(request);
        Template template = loadTemplate(request);
        return renderTemplate(request, template, context);
    }

    private VelocityContext prepareTemplateContext(RenderRequest renderRequest) {
        final VelocityContext context = new VelocityContext(contextPrototype);
        addParametersToVelocityContext(context, renderRequest.getContext());
        return context;
    }

    private void addParametersToVelocityContext(VelocityContext context, Map<String, Object> values) {
        values.forEach(context::put);
    }

    private Template loadTemplate(RenderRequest renderRequest) {
        String localStorageTemplatePath = appendVmIfMissing(renderRequest.getTemplateFileName());
        ReentrantReadWriteLock lock = null;
        if (sdIntegrationEnabled) {
            lock = templateService.getOrDownloadAndLock(renderRequest.getServiceId());
            lock.readLock().lock();
        }

        try {
            log.info("Загрузка шаблона {}", localStorageTemplatePath);
            return engine.getTemplate(localStorageTemplatePath, StandardCharsets.UTF_8.name());
        } catch (ResourceNotFoundException e) {
            val errorMessage = String.format("Файл шаблона %s для сервиса с id %s не был найден "
                            + "во внутреннем хранилище", localStorageTemplatePath,
                    renderRequest.getServiceId());
            throw new TemplateNotFoundException(errorMessage, e);
        } catch (ParseErrorException e) {
            val errorMessage =
                    String.format("Ошибка обработки Velocity-шаблона %s", localStorageTemplatePath);
            throw new RenderTemplateException(errorMessage, e);
        } finally {
            if (Objects.nonNull(lock)) {
                lock.readLock().unlock();
            }
        }
    }

    private String appendVmIfMissing(String text) {
        return text.endsWith(TEMPLATE_EXTENSION) ? text : text + TEMPLATE_EXTENSION;
    }

    private String renderTemplate(RenderRequest renderRequest, Template template,
                                  VelocityContext context) {
        String result = "";
        log.debug("Контекст, используемый для отрисовки шаблона: {}",
                JsonProcessingUtil.toJson(context));
        UsageAwareContext wrappedContext = new UsageAwareContext(context);

        if (Objects.isNull(template)) {
            return result;
        }
        StringWriter writer = new StringWriter();
        template.merge(wrappedContext, writer);
        result = writer.toString();

        if (!wrappedContext.getUnrecognizedKeys().isEmpty()) {
            List<String> unrecognizedPlaceholders = wrappedContext.getUnrecognizedKeys();
            final String errorMsg = String.format(
                    "При генерации из шаблона %s в тексте остались следующие placeholder-ы: %s",
                    renderRequest.getTemplateFileName(), unrecognizedPlaceholders);
            throw new RenderTemplateException(errorMsg);
        }
        return result;
    }
}
