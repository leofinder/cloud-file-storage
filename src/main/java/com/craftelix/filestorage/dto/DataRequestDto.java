package com.craftelix.filestorage.dto;

import com.craftelix.filestorage.validation.ValidPath;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRequestDto {

    @NotBlank(message = "The name cannot be blank.")
    @Size(max = 255, message = "The name must not exceed 255 characters in length.")
    @Pattern(regexp = "^[^/\\\\:*?\"<>|]+$",
            message = "The name must not contain: / \\ : * ? \" < > |")
    private String name;

    @ValidPath
    private String parentPath;

    @NotNull
    private Boolean isFolder;
}
