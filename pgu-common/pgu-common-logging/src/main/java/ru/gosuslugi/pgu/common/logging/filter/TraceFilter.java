package ru.gosuslugi.pgu.common.logging.filter;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class TraceFilter extends GenericFilterBean {

    private final Tracer tracer;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan != null) {
                ((HttpServletResponse) response).addHeader("X-Trace-Id", currentSpan.context().traceIdString());
        }
        chain.doFilter(request, response);
    }

}
