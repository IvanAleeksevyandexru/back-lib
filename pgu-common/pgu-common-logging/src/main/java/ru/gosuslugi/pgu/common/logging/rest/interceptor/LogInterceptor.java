package ru.gosuslugi.pgu.common.logging.rest.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.gosuslugi.pgu.common.logging.rest.RequestResponseLogger;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInterceptor implements HandlerInterceptor {

    private final RequestResponseLogger requestResponseLogger;

    public LogInterceptor(RequestResponseLogger requestResponseLogger) {
        this.requestResponseLogger = requestResponseLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (DispatcherType.REQUEST == request.getDispatcherType() && request.getMethod().equals(HttpMethod.GET.name())) {
            requestResponseLogger.logRequest(request, null);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}
