package com.sajib_4414.expense.tracker.config.exceptions;

import com.sajib_4414.expense.tracker.config.console;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.PermissionError;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.SystemException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorHttpResponse> handleDuplicateEntry(DataIntegrityViolationException ex) {
        ErrorDTO error = ErrorDTO.builder().code("data_error").message("Duplicate data or other error").build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHttpResponse> handleAllOtherError(Exception ex) {
        console.log("Exception happened, here is the detail: "+ex);
        ErrorDTO error = ErrorDTO.builder().code("unknown_error").message("Unknown error occurred").build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorHttpResponse> handleItemNotFound(ItemNotFoundException ex) {
        ErrorDTO error = ErrorDTO.builder().code(ex.getCode()).message(ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PermissionError.class)
    public ResponseEntity<ErrorHttpResponse> handlePermissionError(PermissionError ex) {
        ErrorDTO error = ErrorDTO.builder().code(ex.getCode()).message(ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorHttpResponse> handlePermissionError(NoResourceFoundException ex) {
        ErrorDTO error = ErrorDTO.builder().code("invalid_resouce").message("invalid_resouce").build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorHttpResponse> handleSystemError(SystemException ex) {
        ErrorDTO error = ErrorDTO.builder().code(ex.getCode()).message(ex.getMessage()).build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorHttpResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        ErrorDTO error = ErrorDTO.builder().code("not_allowed").message("Method not allowed").build();
        ErrorHttpResponse errorResponse = ErrorHttpResponse
                .builder()
                .errors(Collections.singletonList(error))
                .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

}
