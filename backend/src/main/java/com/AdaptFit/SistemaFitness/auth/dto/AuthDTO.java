package com.AdaptFit.SistemaFitness.auth.dto;

public class AuthDTO {
    private String token;
    private Long userId;

    public AuthDTO(String token, Long userId){
        this.token = token;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
}
