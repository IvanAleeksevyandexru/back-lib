package ru.gosuslugi.pgu.common.logging.annotation.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.gosuslugi.pgu.common.logging.annotation.Log;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Обработчик логгируемых методов
 */
public class LoggingMethodInterceptor implements MethodInterceptor {

    private static final Pattern METHOD_CLASS_NAME_PATTERN = Pattern.compile("\\$methodClassAndName");
    private static final Pattern ARGS_PATTERN = Pattern.compile("\\$args");
    private static final Pattern METHOD_CLASS_PATTERN = Pattern.compile("\\$methodClass");
    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("\\$methodName");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Logger log = LogAnnotationUtils.getLogger(invocation.getMethod().getDeclaringClass());

        Log annotation = LogAnnotationUtils.getAnnotationRecursive(invocation, Log.class);
        if (annotation == null) {
            throw new IllegalStateException("Log annotation not found");
        }

        if (annotation.printParams()) {
            printParams(invocation, log, annotation);
        }
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            printResult(invocation.getMethod(), log, annotation.resultLevel(),
                    "thrown " + e.getClass().getSimpleName() + "(\"" + e.getMessage() + "\")");
            throw e;
        }

        if (annotation.printResult() && !"void".equals(invocation.getMethod().getReturnType().getName())) {
            printResult(invocation.getMethod(), log, annotation.resultLevel(), result);
        }
        return result;
    }

    private void printParams(MethodInvocation invocation, Logger log, Log annotation) {
        String messageTemplate = getMessageTemplate(annotation);
        String methodClass = invocation.getMethod().getDeclaringClass().getSimpleName();
        String methodName = invocation.getMethod().getName();
        String methodClassAndName = methodClass + '.' + methodName;
        String args = getArgs(invocation.getArguments());

        String message = METHOD_CLASS_NAME_PATTERN.matcher(messageTemplate).replaceAll(methodClassAndName);
        message = METHOD_CLASS_PATTERN.matcher(message).replaceAll(methodClass);
        message = METHOD_NAME_PATTERN.matcher(message).replaceAll(methodName);
        message = ARGS_PATTERN.matcher(message).replaceAll(args);

        LogAnnotationUtils.getLogMethod(log, annotation.paramsLevel()).log(message, invocation.getArguments());
    }

    private String getMessageTemplate(Log annotation) {
        if (StringUtils.hasText(annotation.value())) {
            return annotation.value();
        }
        return annotation.message();
    }

    private void printResult(Method method, Logger log, Level resultLevel, Object result) {
        LogAnnotationUtils
                .getLogMethod(log, resultLevel)
                .log("Result of " + getMethodName(method) + "(): {}", result);
    }

    private String getArgs(Object[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arguments.length; i++) {
                sb.append("{}");
                if (i < arguments.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }

    private String getMethodName(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

}
