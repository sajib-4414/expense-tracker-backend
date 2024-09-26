package com.sajib_4414.expense.tracker.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajib_4414.expense.tracker.config.exceptions.ErrorDTO;
import com.sajib_4414.expense.tracker.config.exceptions.ErrorHttpResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
//this class is more like a middleware
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    //filter chain is more like next() method
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("do internal filter invoked/....");

        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                System.out.println("No token given, all good, passing to next filter");
                filterChain.doFilter(request,response); //not attaching any user, passing to the next filter/middleware
                return;
            }
            jwt =authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            System.out.println("everything done in jwt, passed to next filter");
            filterChain.doFilter(request,response);

        } catch (ExpiredJwtException ex) {
            // Handle the exception and return the error response directly
            ErrorDTO error = ErrorDTO.builder()
                    .code("token_expired")
                    .message("Token Expired, " + ex.getMessage())
                    .build();
            ErrorHttpResponse errorResponse = ErrorHttpResponse.builder()
                    .errors(Collections.singletonList(error))
                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return; // Prevent further processing
        }
        catch (SignatureException ex) {
            // Handle the exception and return the error response directly
            ErrorDTO error = ErrorDTO.builder()
                    .code("token_wrong")
                    .message("Authentication error, " + ex.getMessage())
                    .build();
            ErrorHttpResponse errorResponse = ErrorHttpResponse.builder()
                    .errors(Collections.singletonList(error))
                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return; // Prevent further processing
        }
        catch (InternalAuthenticationServiceException ex) {

            System.out.println("this exception happened...");
            // Handle the exception and return the error response directly
            ErrorDTO error = ErrorDTO.builder()
                    .code("token_wrong_v2")
                    .message("Authentication error, " + ex.getMessage())
                    .build();
            ErrorHttpResponse errorResponse = ErrorHttpResponse.builder()
                    .errors(Collections.singletonList(error))
                    .build();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return; // Prevent further processing
        }

    }
}
