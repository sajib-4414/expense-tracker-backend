package com.sajib_4414.expense.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajib_4414.expense.tracker.payload.LoginRequest;
import com.sajib_4414.expense.tracker.payload.RegisterRequest;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public  void setUp() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setUsername("john");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");


        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform
                        (post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                        )
                .andExpect(status().isCreated());

    }

    @Test
    public void testLogin() throws Exception {


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");
        loginRequest.setPassword("password123");
        String jsonLoginRequest = objectMapper.writeValueAsString(loginRequest);
        mockMvc.perform
                        (post("/api/v1/auth/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonLoginRequest)
                        )
                .andExpect(status().isOk());

    }
}
