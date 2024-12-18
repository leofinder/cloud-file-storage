package com.craftelix.filestorage.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PathValidator implements ConstraintValidator<ValidPath, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.startsWith("/") && value.endsWith("/");
    }
}
