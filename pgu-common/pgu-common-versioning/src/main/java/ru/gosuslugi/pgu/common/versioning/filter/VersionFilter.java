package ru.gosuslugi.pgu.common.versioning.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.gosuslugi.pgu.common.versioning.dto.RequestEnvironment;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Order(-1)
public class VersionFilter extends GenericFilterBean {

    private static final String ORIGIN_ENVIRONMENT_HEADER = "origin-environment";

    private final String appEnv;

    private final RequestEnvironment requestEnvironment;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String envFromRequest = ((HttpServletRequest) request).getHeader(ORIGIN_ENVIRONMENT_HEADER);
        if (StringUtils.hasText(envFromRequest)) {
            requestEnvironment.setEnv(envFromRequest);
        }
        ((HttpServletResponse) response).addHeader(ORIGIN_ENVIRONMENT_HEADER, appEnv);
        chain.doFilter(request, response);
    }
}
