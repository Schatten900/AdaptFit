package com.AdaptFit.SistemaFitness.common;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException() {
        super();
    }
}
