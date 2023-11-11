package com.maruhxn.boardserver.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 2, max = 10)
    private String username;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String password;

    @NotEmpty
    @Size(min = 2, max = 20)
    private String confirmPassword;
}
