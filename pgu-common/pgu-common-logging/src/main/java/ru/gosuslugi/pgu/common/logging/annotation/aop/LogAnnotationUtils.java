package ru.gosuslugi.pgu.common.logging.annotation.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * Утилитарный метод для работы с аннотациями логгинга
 */
public class LogAnnotationUtils {

    static <T extends Annotation> T  getAnnotation(MethodInvocation invocation, Class<T> annotationClass) {
        return getAnnotation(invocation.getMethod(), invocation.getThis().getClass(), annotationClass);
    }

    static <T extends Annotation> T getAnnotationRecursive(MethodInvocation invocation, Class<T> annotationClass) {
        T result = getAnnotation(invocation.getMethod(), invocation.getThis().getClass(), annotationClass);
        if (result == null) {
            Annotation[] annotations = invocation.getMethod().getAnnotations();
            if (annotations != null && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    T[] nestedAnnotations = annotation.annotationType().getAnnotationsByType(annotationClass);
                    if (nestedAnnotations.length > 0) {
                        return nestedAnnotations[0];
                    }
                }
            }
        }
        return result;
    }

    static <T extends Annotation> T getAnnotation(Method method, Class<?> targetClass, Class<T> annotationClass) {
        try {
            T[] annotations = method.getAnnotationsByType(annotationClass);
            if (annotations.length == 0) {
                annotations = targetClass.getMethod(method.getName(), method.getParameterTypes())
                        .getAnnotationsByType(annotationClass);
                if (annotations.length == 0) {
                    annotations = targetClass.getAnnotationsByType(annotationClass);
                }
            }
            return annotations.length > 0 ? annotations[0] : null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SafeVarargs
    static boolean hasAnnotation(Method method, Class<?> targetClass, Class<? extends Annotation>... annotationsToSearch) {
        if (method == null) {
            return false;
        }
        try {
            List<Class<? extends Annotation>> annotationsToSearchList = Arrays.asList(annotationsToSearch);
            if (targetClass != null
                    && isAnnotationExist(annotationsToSearchList, targetClass.getAnnotations())
                    && Modifier.isPublic(method.getModifiers())) {
                return true;
            }
            if (isAnnotationExist(annotationsToSearchList, method.getAnnotations())) {
                return true;
            }
            if (targetClass != null
                    && isAnnotationExist(annotationsToSearchList, targetClass.getMethod(method.getName(), method.getParameterTypes()).getAnnotations())) {
                return true;
            }
        } catch (NoSuchMethodException ignored) {}
        return false;
    }

    private static boolean isAnnotationExist(List<Class<? extends Annotation>> annotationsToSearchList, Annotation[] annotations) {
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotationsToSearchList.contains(annotation.annotationType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Logger getLogger(Class<?> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }


    public static LogMethod getLogMethod(Logger log, Level level) {
        switch (level) {
            case ERROR: return log::error;
            case WARN: return log::warn;
            case INFO: return log::info;
            case DEBUG: return log::debug;
            case TRACE: return log::trace;
            default: throw new IllegalArgumentException("Unsupported log level");
        }
    }

    public interface LogMethod {
        void log(String format, Object... arguments);
    }
}
