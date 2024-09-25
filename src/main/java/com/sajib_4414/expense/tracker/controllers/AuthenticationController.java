package com.sajib_4414.expense.tracker.controllers;


import com.sajib_4414.expense.tracker.payload.LoginRequest;
import com.sajib_4414.expense.tracker.payload.LoginResponse;
import com.sajib_4414.expense.tracker.services.AuthenticationService;
import com.sajib_4414.expense.tracker.payload.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth" )
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }



}
