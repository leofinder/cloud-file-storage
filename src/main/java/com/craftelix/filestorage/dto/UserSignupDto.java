package com.craftelix.filestorage.dto;

import com.craftelix.filestorage.validation.PasswordMatches;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UserSignupDto {

    @Size(min = 3, message = "Username must be at least 3 characters long")
    @Pattern(
            regexp = "^[a-z0-9.@_]+$",
            message = "Username can only contain lowercase letters, digits, and the characters ._@"
    )
    private String username;

    @Size(min = 3, message = "Password must be at least 3 characters long")
    @Pattern(
            regexp = "^\\S+$",
            message = "Password must not contain whitespace characters"
    )
    private String password;

    private String confirmPassword;
}

