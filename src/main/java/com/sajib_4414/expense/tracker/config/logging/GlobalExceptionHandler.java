package com.sajib_4414.expense.tracker.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");

        log.error("UNHANDLED_ERROR traceId={} path={} message={}",
                traceId,
                request.getRequestURI(),
                ex.getMessage(),
                ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("traceId", traceId);
        body.put("error", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}