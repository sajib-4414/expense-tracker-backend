package com.sajib_4414.expense.tracker.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        MDC.put("traceId", traceId);
        MDC.put("method", request.getMethod());
        MDC.put("path", request.getRequestURI());

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = wrappedResponse.getStatus();

            // Spring has already read the body by now - safe to get from cache
            byte[] bodyBytes = wrappedRequest.getContentAsByteArray();
            String requestBody = "";
            if (bodyBytes.length > 0) {
                try {
                    requestBody = new String(bodyBytes,
                            request.getCharacterEncoding() != null
                                    ? request.getCharacterEncoding()
                                    : "UTF-8");
                } catch (Exception e) {
                    requestBody = "[could not read body]";
                }
            }

            if (status >= 400) {
                log.warn("RESPONSE traceId={} path={} method={} status={} duration={}ms payload={}",
                        traceId,
                        request.getRequestURI(),
                        request.getMethod(),
                        status,
                        duration,
                        sanitize(requestBody));
            } else {
                log.info("RESPONSE traceId={} path={} method={} status={} duration={}ms payload={}",
                        traceId,
                        request.getRequestURI(),
                        request.getMethod(),
                        status,
                        duration,
                        sanitize(requestBody));
            }

            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    private String sanitize(String body) {
        if (body == null || body.isBlank()) return "";
        return body.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"***\"")
                .replaceAll("\"token\"\\s*:\\s*\"[^\"]*\"", "\"token\":\"***\"");
    }
}