package com.sajib_4414.expense.tracker.services;

import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.ItemNotFoundException;
import com.sajib_4414.expense.tracker.config.exceptions.customexceptions.SystemException;
import com.sajib_4414.expense.tracker.payload.AuthResponse;
import com.sajib_4414.expense.tracker.payload.RegisterRequest;
import com.sajib_4414.expense.tracker.config.auth.JWTService;
import com.sajib_4414.expense.tracker.models.user.*;
import com.sajib_4414.expense.tracker.payload.LoginRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

    public AuthResponse authenticate(LoginRequest request) {

            var token = new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
            try{
                Authentication result = authenticationManager.authenticate(token);

            }catch (InternalAuthenticationServiceException ex){
                throw new ItemNotFoundException("User not found");
            }
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new ItemNotFoundException("User not found"));
            var jwtToken = jwtService.generateToken(user);
            return  AuthResponse.builder().token(jwtToken).user(user).build();


            // Continue with your logic here


    }

    //default register only allows to get a role of User.
    //later we will add more roles to a User, only via endpoint
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new SystemException("User role yet not defined"));

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
            return  AuthResponse.builder().token(jwtToken).user(user).build();



    }
}
