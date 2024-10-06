package com.sajib_4414.expense.tracker.config;

import com.sajib_4414.expense.tracker.config.auth.JWTAuthFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class GlobalSecurityConfiguration {

    //using final and required args construction means spring will automatically inject
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.disable())

                .authorizeHttpRequests(auth ->
                        auth
                        .requestMatchers(HttpMethod.GET,"/api/v1/demo").hasAnyAuthority(Roles.USER, Roles.ADMIN)
                        .requestMatchers("/api/v1/categories/all-categories").hasAuthority(Roles.ADMIN)
                         .requestMatchers("/api/v1/categories/**").hasAnyAuthority(Roles.USER, Roles.ADMIN)
                        .requestMatchers("/api/v1/income/income-source/**").hasAuthority(Roles.USER)
                         .requestMatchers("/api/v1/income/**").hasAuthority(Roles.USER)
                        .requestMatchers("/api/v1/expenses/**").hasAnyAuthority(Roles.USER, Roles.ADMIN)
                         .requestMatchers("/api/v1/auth/**").permitAll()
                         .requestMatchers("/error").permitAll()
//                         //the below two line means, anything else needs to be authenticated
//                        //for now we dont need these kind of config, as we are already more explicit like we need authentication
//                        //with ROLE_USER
//                        .anyRequest()
//                        .authenticated()
//                                .anyRequest().permitAll()
                )
                .sessionManagement( sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }

}
