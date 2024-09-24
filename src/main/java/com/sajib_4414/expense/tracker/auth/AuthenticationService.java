package com.sajib_4414.expense.tracker.auth;

import com.sajib_4414.expense.tracker.config.JWTService;
import com.sajib_4414.expense.tracker.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        try {
            System.out.println("here printing credentials"+request);
            var token = new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
            System.out.println("here printing token"+token);
            var result = authenticationManager.authenticate(token);
            System.out.println("here after authenticating"+result);
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return  AuthenticationResponse.builder().token(jwtToken).build();
            // Continue with your logic here
        }  catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());

        }
        return null;

    }

    //default register only allows to get a role of User.
    //later we will add more roles to a User, only via endpoint
    public AuthenticationResponse register(RegisterRequest request) {

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new RuntimeException("User role yet not defined"));

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        //now create user role
        UserRole userRole = UserRole.builder()
                .role(role)
                .user(user)
                .build();
        userRoleRepository.save(userRole);

        var jwtToken = jwtService.generateToken(user);
        return  AuthenticationResponse.builder().token(jwtToken).build();
    }
}
