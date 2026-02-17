package com.AdaptFit.SistemaFitness.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "A senha deve ter pelo menos 6 caracteres, incluindo pelo menos 1 letra maiúscula, 1 caractere especial e 1 dígito";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
