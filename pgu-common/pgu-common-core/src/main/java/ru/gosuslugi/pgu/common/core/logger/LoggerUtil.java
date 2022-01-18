package ru.gosuslugi.pgu.common.core.logger;

import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * Утилитарный сервис для работы с логгером. По сути декоратор над методами {@link Logger}.
 * Осущесвляет отложенный вызов получения сообщения для записи в лог.
 *
 * Скопировано из проекта Delirium
 */
public class LoggerUtil {

    public static void info(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isInfoEnabled()) {
            logger.info(messageSupplier.get());
        }
    }

    public static void info(Logger logger, Supplier<String> messageSupplier, Throwable throwable) {
        if (logger.isInfoEnabled()) {
            logger.info(messageSupplier.get(), throwable);
        }
    }

    public static void trace(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isTraceEnabled()) {
            logger.trace(messageSupplier.get());
        }
    }

    public static void trace(Logger logger, Supplier<String> messageSupplier, Throwable throwable) {
        if (logger.isTraceEnabled()) {
            logger.trace(messageSupplier.get(), throwable);
        }
    }

    public static void debug(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isDebugEnabled()) {
            logger.debug(messageSupplier.get());
        }
    }

    public static void debug(Logger logger, Supplier<String> messageSupplier, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug(messageSupplier.get(), throwable);
        }
    }

    public static void warn(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isWarnEnabled()) {
            logger.warn(messageSupplier.get());
        }
    }

    public static void warn(Logger logger, Supplier<String> messageSupplier, Throwable throwable) {
        if (logger.isWarnEnabled()) {
            logger.warn(messageSupplier.get(), throwable);
        }
    }

    public static void error(Logger logger, Supplier<String> messageSupplier) {
        if (logger.isErrorEnabled()) {
            logger.error(messageSupplier.get());
        }
    }

    public static void error(Logger logger, Supplier<String> messageSupplier, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error(messageSupplier.get(), throwable);
        }
    }

}