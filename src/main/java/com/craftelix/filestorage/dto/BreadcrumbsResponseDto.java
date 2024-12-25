package com.craftelix.filestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreadcrumbsResponseDto {

    private final String name;

    private final String path;
}
