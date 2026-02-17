package com.AdaptFit.SistemaFitness.common.api;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private String message;
    private T data;
    private ApiMessageType type;

    public ApiResponse(String message) {
        this.message = message;
    }
    public ApiResponse(T data) {
        this.data = data;
    }
    public ApiResponse(ApiMessageType type) {
        this.type = type;
    }

    public ApiResponse(String message, ApiMessageType type) {
        this.type = type;
        this.message = message;
    }

}
