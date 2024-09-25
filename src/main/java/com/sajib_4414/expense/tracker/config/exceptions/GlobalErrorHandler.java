package com.sajib_4414.expense.tracker.config.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleIllegalArgument(HttpMediaTypeNotAcceptableException ex) {
        return ResponseEntity.badRequest().body("bad errorr");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorHttpResponse> handleBadBody(HttpMessageNotReadableException ex) {
        ErrorDTO error = ErrorDTO.builder().code("body_missing").message("Request body is missing").build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorHttpResponse> handleBadCredentials(BadCredentialsException ex) {
        ErrorDTO error = ErrorDTO.builder().code("bad_credentials").message("Request credentials not correct, "+ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorHttpResponse> handleBadCredentials(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErrorDTO.builder()
                        .code(fieldError.getCode())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorHttpResponse errorResponse = ErrorHttpResponse.builder()
                .errors(errors)
                .build();
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorHttpResponse> handleExpiredJwtException(ExpiredJwtException ex) {

        ErrorDTO error = ErrorDTO.builder().code("token_expired").message("Token Expired, "+ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }


}
