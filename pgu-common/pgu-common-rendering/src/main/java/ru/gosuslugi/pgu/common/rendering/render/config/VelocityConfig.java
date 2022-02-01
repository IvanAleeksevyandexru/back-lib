package ru.gosuslugi.pgu.common.rendering.render.config;

import ru.gosuslugi.pgu.common.rendering.render.config.props.VelocityProperties;
import ru.gosuslugi.pgu.common.rendering.render.template.function.AddressService;
import ru.gosuslugi.pgu.common.rendering.render.template.function.DateService;
import ru.gosuslugi.pgu.common.rendering.render.template.function.LogDebugConsole;
import ru.gosuslugi.pgu.common.rendering.render.template.function.MoneyService;
import ru.gosuslugi.pgu.common.rendering.render.template.function.StringService;
import ru.gosuslugi.pgu.common.rendering.render.template.function.XmlService;
import ru.gosuslugi.pgu.common.rendering.template.config.props.TemplateServiceProperties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.SortTool;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Настройка бинов Apache Velocity.
 */
@Configuration
@EnableConfigurationProperties(value = {VelocityProperties.class, TemplateServiceProperties.class})
public class VelocityConfig {
    public static final String DATE_TOOL_VAR_NAME = "dateTool";
    public static final String DATE_SERVICE_VAR_NAME = "dateService";
    public static final String STRING_SERVICE_VAR_NAME = "stringService";
    public static final String MONEY_SERVICE_VAR_NAME = "moneyService";
    public static final String XML_SERVICE_VAR_NAME = "xmlService";
    public static final String CONSOLE_VAR_NAME = "console";
    public static final String ADDRESS_SERVICE_VAR_NAME = "addressService";
    public static final String MATH_TOOL_VAR_NAME = "mathTool";
    public static final String DATE_COMPARE_TOOL_VAR_NAME = "compareDateTool";
    public static final String STRING_TOOL_VAR_NAME = "strTool";
    public static final String APACHE_STRING_TOOL_VAR_NAME = "apacheStrTool";
    public static final String INTEGER_VAR_NAME = "Integer";
    public static final String SORT_TOOL_VAR_NAME = "sorter";

    /**
     * Переносит в настройки {@link VelocityEngine#setProperty(String, Object)} опции, которые
     * требуются для одного из двух методов поиска файлов шаблонов: {@link
     * VelocityProperties.ResourceLoader#FILE} или {@link VelocityProperties.ResourceLoader#CLASS}.
     *
     * @param velocityProps настройки движка.
     * @return инициализированный движок.
     */
    @Bean
    public VelocityEngine velocityEngine(VelocityProperties velocityProps,
                                         TemplateServiceProperties templateServiceProps) {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty("runtime.log.logsystem.log4j.logger", "root");
        if (VelocityProperties.ResourceLoader.FILE.equals(velocityProps.getResourceLoader())) {
            engine.setProperty("resource.loaders",
                    VelocityProperties.ResourceLoader.FILE.name().toLowerCase());
            engine.setProperty("file.resource.loader.class",
                    velocityProps.getFileResourceLoaderClass());
            engine.setProperty("file.resource.loader.path",
                    templateServiceProps.getFileResourceLoaderPath());
            engine.setProperty("resource.loader.file.cache",
                    velocityProps.getFileResourceLoaderClass());
            engine.setProperty("resource.loader.file.modification_check_interval",
                    velocityProps.getResourceLoaderFileModificationCheckInterval());
        }
        if (VelocityProperties.ResourceLoader.CLASS.equals(velocityProps.getResourceLoader())) {
            engine.setProperty("resource.loader",
                    VelocityProperties.ResourceLoader.CLASS.name().toLowerCase());
            engine.setProperty("class.resource.loader.class",
                    velocityProps.getClassResourceLoaderClass());
        }
        engine.init();
        return engine;
    }

    /**
     * Создает прототип контекста.
     * <p>
     * Определяет функции, такие как {@value APACHE_STRING_TOOL_VAR_NAME}, {@value
     * MONEY_SERVICE_VAR_NAME}.
     *
     * @return прототип контекста.
     * @see ru.gosuslugi.pgu.xmlservice.render.template.function
     */
    @Bean
    public VelocityContext prototypeTemplateContext() {
        VelocityContext context = new VelocityContext();
        context.put(DATE_TOOL_VAR_NAME, new DateTool());
        context.put(DATE_SERVICE_VAR_NAME, new DateService());
        context.put(STRING_SERVICE_VAR_NAME, new StringService());
        context.put(CONSOLE_VAR_NAME, new LogDebugConsole());
        context.put(ADDRESS_SERVICE_VAR_NAME, new AddressService());
        context.put(MATH_TOOL_VAR_NAME, new MathTool());
        context.put(MONEY_SERVICE_VAR_NAME, new MoneyService());
        context.put(DATE_COMPARE_TOOL_VAR_NAME, new ComparisonDateTool());
        context.put(STRING_TOOL_VAR_NAME, StringUtils.class);
        context.put(APACHE_STRING_TOOL_VAR_NAME, org.apache.commons.lang3.StringUtils.class);
        context.put(INTEGER_VAR_NAME, Integer.class);
        context.put(XML_SERVICE_VAR_NAME, new XmlService());
        context.put(SORT_TOOL_VAR_NAME, new SortTool());
        return context;
    }
}
