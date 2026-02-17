package com.AdaptFit.SistemaFitness.common.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException() {
        super();
    }
}
