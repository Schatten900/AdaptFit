package com.AdaptFit.SistemaFitness.auth;

import com.AdaptFit.SistemaFitness.auth.dto.AuthDTO;
import com.AdaptFit.SistemaFitness.auth.dto.LoginDTO;
import com.AdaptFit.SistemaFitness.auth.dto.RegisterDTO;
import com.AdaptFit.SistemaFitness.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterDTO req) {
        authService.register(req);
        return ResponseEntity.ok(new ApiResponse<Void>("Usuário registrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO>> login(@Valid @RequestBody LoginDTO req) {
        AuthDTO auth = authService.login(req);
        return ResponseEntity.ok(new ApiResponse<>(auth));
    }
}
