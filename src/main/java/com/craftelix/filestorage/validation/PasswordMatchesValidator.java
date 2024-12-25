package com.craftelix.filestorage.validation;

import com.craftelix.filestorage.dto.UserSignupDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSignupDto> {

    @Override
    public boolean isValid(UserSignupDto userSignupDto, ConstraintValidatorContext context) {
        return userSignupDto.getPassword() != null && userSignupDto.getPassword().equals(userSignupDto.getConfirmPassword());
    }
}