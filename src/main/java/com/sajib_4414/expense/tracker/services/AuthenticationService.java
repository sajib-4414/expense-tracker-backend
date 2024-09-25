package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.payload.RegisterRequest;
import com.sajib_4414.expense.tracker.config.auth.JWTService;
import com.sajib_4414.expense.tracker.models.user.*;
import com.sajib_4414.expense.tracker.payload.LoginRequest;
import com.sajib_4414.expense.tracker.payload.LoginResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse authenticate(LoginRequest request) {

        try {
            var token = new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
            Authentication result = authenticationManager.authenticate(token);
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return  LoginResponse.builder().token(jwtToken).build();
            // Continue with your logic here
        }  catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());

        }
        return null;

    }

    //default register only allows to get a role of User.
    //later we will add more roles to a User, only via endpoint
    @Transactional
    public LoginResponse register(RegisterRequest request) {

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new RuntimeException("User role yet not defined"));

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        user.setUserRoles(Collections.singleton(userRole));
        userRepository.save(user);
        //now create user role


        var jwtToken = jwtService.generateToken(user);
        return  LoginResponse.builder().token(jwtToken).build();
    }
}
