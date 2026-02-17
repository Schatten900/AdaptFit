package com.AdaptFit.SistemaFitness.user;

import com.AdaptFit.SistemaFitness.common.exception.AuthenticationException;
import com.AdaptFit.SistemaFitness.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new AuthenticationException("Usuario não autenticado");
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User))
            throw new ValidationException("Autenticação invalida");
        return (User) principal;
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public User updateUser(User user) {
        // Implement update logic
        return userRepository.save(user);
    }
}
