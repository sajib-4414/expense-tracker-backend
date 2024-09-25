package com.sajib_4414.expense.tracker.payload;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "firstname is required")
    private String firstname;

    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "lastname is required")
    private String lastname;

    @NotEmpty(message = "password is required")
    private String password;

}
