package ru.gosuslugi.pgu.common.logging.annotation;

import java.lang.annotation.*;
import org.slf4j.event.Level;

/**
 * Аннотация для автоматического логгировани входящих и исходящих параметров
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Log {

    /**
     * Шаблон сообщения о вызове аннотированного метода. Можно содержать статический текст или макросы:
     *
     * $methodClassAndName - имя класса и метода в формате ClassName.methodName
     * $methodClass - имя класса
     * $methodName - имя метода
     * $args - список входящих параметров через запятую.
     *
     * Также можно вставлять символы {} вместо параметров, например "Method params: id = {}, name = {}"
     * Первая комбинация {} будет заменена на первый параметр, вторая - на второй и т.д.
     */
    String message() default "Call $methodClassAndName($args)";

    String value() default "";

    /**
     * Если true, то логгировать факт вызова метода и входящие параметры (если есть)
     */
    boolean printParams() default true;

    /**
     * Уровень логгирования для входящих параметров
     */
    Level paramsLevel() default Level.INFO;

    /**
     * Если true, то логгировать результат выполнени метода (если не void)
     */
    boolean printResult() default true;

    /**
     * Уровень логгирования для результата выполнения метода
     */
    Level resultLevel() default Level.DEBUG;


    @Retention(RetentionPolicy.RUNTIME)
    @Log
    @interface Debug { }

    @Retention(RetentionPolicy.RUNTIME)
    @Log(paramsLevel = Level.INFO, resultLevel = Level.INFO)
    @interface Info { }

}
