package ru.gosuslugi.pgu.common.rendering.render.template.function;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogDebugConsole implements Console {

    public LogDebugConsole() {
    }

    @Override
    public void log(String string) {
        log.debug(string);
    }

    @Override
    public void log(String template, Object... objects) {
        log.debug(template, objects);
    }
}
