package com.craftelix.filestorage.dto;

import com.craftelix.filestorage.validation.ValidPath;
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

    @NotNull
    @Pattern(regexp = "^[^/\\\\:*?\"<>|]+$",
            message = "The name must not contain: / \\ : * ? \" < > |.")
    @Size(min = 1, max = 255,
            message = "The name must be between 1 and 255 characters long.")
    private String name;

    @NotNull
    @Pattern(regexp = "^[^/\\\\:*?\"<>|]+$",
            message = "The new name must not contain: / \\ : * ? \" < > |.")
    @Size(min = 1, max = 255,
            message = "The new name must be between 1 and 255 characters long.")
    private String newName;

    @ValidPath
    private String parentPath;

    @NotNull
    private Boolean isFolder;
}
