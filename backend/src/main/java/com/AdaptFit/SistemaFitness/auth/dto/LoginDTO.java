package com.AdaptFit.SistemaFitness.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @Email
    private String email;
    @NotBlank
    private String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
