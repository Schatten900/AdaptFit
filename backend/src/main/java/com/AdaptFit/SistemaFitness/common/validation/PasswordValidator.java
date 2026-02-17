package com.AdaptFit.SistemaFitness.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final int MIN_LENGTH = 6;
    private static final String UPPERCASE_PATTERN = "[A-Z]";
    private static final String SPECIAL_CHAR_PATTERN = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";
    private static final String DIGIT_PATTERN = "[0-9]";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true;
        }

        if (password.length() < MIN_LENGTH) {
            return false;
        }

        boolean hasUppercase = password.matches(".*" + UPPERCASE_PATTERN + ".*");
        boolean hasSpecialChar = password.matches(".*" + SPECIAL_CHAR_PATTERN + ".*");
        boolean hasDigit = password.matches(".*" + DIGIT_PATTERN + ".*");

        return hasUppercase && hasSpecialChar && hasDigit;
    }
}
