package ru.gosuslugi.pgu.common.logging.annotation.aop;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;
import ru.gosuslugi.pgu.common.logging.annotation.Log;

import java.lang.reflect.Method;

/**
 * Класс участвует в поиске методов, помеченных аннотациями и возвращает обработчик для них
 */
public class LoggingPointcutAdvisor extends AbstractPointcutAdvisor {

    private final LoggingMethodInterceptor interceptor;

    public LoggingPointcutAdvisor(LoggingMethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return LogAnnotationUtils.hasAnnotation(method, targetClass, Log.class, Log.Info.class, Log.Debug.class);
        }
    };

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return interceptor;
    }

    @Override
    public int getOrder() {
        return 1000;
    }

}
