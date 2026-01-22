package com.AdaptFit.SistemaFitness.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
    @Email
    private String email;

    @NotBlank
    private String username;

    @Size(min=8)
    private String password;

    @NotBlank
    private String confirmPassword;

    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}
