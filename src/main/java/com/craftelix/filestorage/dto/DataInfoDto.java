package com.craftelix.filestorage.dto;

import com.craftelix.filestorage.entity.User;
import com.craftelix.filestorage.util.PathUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataInfoDto {

    private String name;

    private String parentPath;

    private String path;

    private Boolean isFolder;

    private Long size;

    public DataInfoDto(String name, String parentPath, Boolean isFolder, Long size) {
        this.name = name;
        this.parentPath = parentPath;
        this.path = PathUtil.getFullPath(parentPath, name, isFolder);
        this.isFolder = isFolder;
        this.size = size;
    }
}
