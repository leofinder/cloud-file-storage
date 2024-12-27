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
public class DataRenameRequestDto {

    @NotBlank(message = "The name cannot be blank.")
    @Size(max = 255, message = "The name must not exceed 255 characters in length.")
    @Pattern(regexp = "^[^/\\\\:*?\"<>|]+$", message = "The name must not contain: / \\ : * ? \" < > |")
    @Pattern(regexp = "^(?!\\.+$).*", message = "The name cannot consist only of dots.")
    private String name;

    @NotBlank(message = "The new name cannot be blank.")
    @Size(max = 255, message = "The new name must not exceed 255 characters in length.")
    @Pattern(regexp = "^[^/\\\\:*?\"<>|]+$", message = "The new name must not contain: / \\ : * ? \" < > |")
    @Pattern(regexp = "^(?!\\.+$).*", message = "The new name cannot consist only of dots.")
    private String newName;

    @ValidPath
    private String parentPath;

    @NotNull
    private Boolean isFolder;
}
