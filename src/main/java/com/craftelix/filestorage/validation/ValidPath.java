package com.craftelix.filestorage.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PathValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPath {

    String message() default "Invalid path";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}