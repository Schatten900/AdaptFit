package com.AdaptFit.SistemaFitness.auth.dto;

import com.AdaptFit.SistemaFitness.common.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @ValidPassword
    private String password;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmPassword;

    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}
