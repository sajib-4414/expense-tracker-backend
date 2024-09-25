package com.sajib_4414.expense.tracker.controllers;


import com.sajib_4414.expense.tracker.payload.LoginRequest;
import com.sajib_4414.expense.tracker.payload.LoginResponse;
import com.sajib_4414.expense.tracker.services.AuthenticationService;
import com.sajib_4414.expense.tracker.payload.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> register(@RequestBody LoginRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @RestController
    @RequestMapping("/api/v1/demo")
    public static class DemoController {
        @GetMapping("")
        public ResponseEntity<String> sayHello(){
            return ResponseEntity.ok("hello from secured endpoint");
        }
    }
}
