package ru.gosuslugi.pgu.common.rendering.render.template.function;

public interface Console {

    void log(String string);

    void log(String template, Object... objects);

}
