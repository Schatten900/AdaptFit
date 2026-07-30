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

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>(data);
        response.message = message;
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, ApiMessageType.ERROR);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(message, ApiMessageType.WARNING);
    }
}
