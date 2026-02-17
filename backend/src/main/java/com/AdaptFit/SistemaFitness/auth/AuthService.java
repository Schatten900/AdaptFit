package com.AdaptFit.SistemaFitness.auth;

import com.AdaptFit.SistemaFitness.auth.dto.AuthDTO;
import com.AdaptFit.SistemaFitness.auth.dto.LoginDTO;
import com.AdaptFit.SistemaFitness.auth.dto.RegisterDTO;
import com.AdaptFit.SistemaFitness.common.exception.BusinessException;
import com.AdaptFit.SistemaFitness.common.exception.NotFoundException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import com.AdaptFit.SistemaFitness.jwt.JwtService;
import com.AdaptFit.SistemaFitness.user.User;
import com.AdaptFit.SistemaFitness.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder encoder,
                        JwtService jwtService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterDTO req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new ValidationException("Usuario já existe");
        }
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new ValidationException("Senhas devem ser iguais");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));

        userRepository.save(user);
    }

    public AuthDTO login(LoginDTO req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuário não existe"));

        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new BusinessException("Dados inválidos");

        String token = jwtService.generateToken(user);
        return new AuthDTO(token, user.getId());
    }
}