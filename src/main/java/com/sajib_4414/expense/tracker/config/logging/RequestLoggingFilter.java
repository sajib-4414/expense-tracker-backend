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

        // Wrap request so body can be read multiple times
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Put values in MDC so every log line in this request gets them
        MDC.put("traceId", traceId);
        MDC.put("method", request.getMethod());
        MDC.put("path", request.getRequestURI());

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            String requestBody = getBody(wrappedRequest.getContentAsByteArray(), wrappedRequest.getCharacterEncoding());
            int status = wrappedResponse.getStatus();

            if (status >= 400) {
                log.warn("REQUEST path={} method={} status={} duration={}ms traceId={} payload={}",
                        request.getRequestURI(),
                        request.getMethod(),
                        status,
                        duration,
                        traceId,
                        sanitize(requestBody));
            } else {
                log.info("REQUEST path={} method={} status={} duration={}ms traceId={}",
                        request.getRequestURI(),
                        request.getMethod(),
                        status,
                        duration,
                        traceId);
            }

            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }

    private String getBody(byte[] content, String encoding) {
        if (content.length == 0) return "";
        try {
            return new String(content, encoding != null ? encoding : "UTF-8");
        } catch (Exception e) {
            return "[unreadable]";
        }
    }

    // Strip sensitive fields before logging
    private String sanitize(String body) {
        if (body == null || body.isBlank()) return "";
        return body.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"***\"")
                .replaceAll("\"token\"\\s*:\\s*\"[^\"]*\"", "\"token\":\"***\"");
    }
}
