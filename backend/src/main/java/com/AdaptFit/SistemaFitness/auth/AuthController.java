package com.AdaptFit.SistemaFitness.auth;

import com.AdaptFit.SistemaFitness.auth.dto.AuthDTO;
import com.AdaptFit.SistemaFitness.auth.dto.LoginDTO;
import com.AdaptFit.SistemaFitness.auth.dto.RegisterDTO;
import com.AdaptFit.SistemaFitness.common.ApiResponse;
import com.AdaptFit.SistemaFitness.common.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterDTO req) {
        authService.register(req);
        return ResponseEntity.ok(new ApiResponse<Void>("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO>> login(@RequestBody LoginDTO req) {
        AuthDTO auth = authService.login(req);
        return ResponseEntity.ok(new ApiResponse<>(auth));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(new ApiResponse<Void>(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new ApiResponse<Void>("Internal server error: " + e.getMessage()));
    }
}
