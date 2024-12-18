package com.craftelix.filestorage.dto;

import com.craftelix.filestorage.util.FileSizeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponseDto {

    private String name;

    private String parentPath;

    private String path;

    private Boolean isFolder;

    private Long size;

    public String getFormattedSize() {
        return FileSizeFormatter.formatSize(size);
    }
}
