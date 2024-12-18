package com.craftelix.filestorage.service;

import com.craftelix.filestorage.dto.BreadcrumbsResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BreadcrumbService {

    public List<BreadcrumbsResponseDto> build(String path) {
        String[] pathParts = path.split("/");

        StringBuilder accumulatedPath = new StringBuilder();
        accumulatedPath.append("/");

        List<BreadcrumbsResponseDto> breadcrumbs = new ArrayList<>();

        for (String part : pathParts) {
            if (!part.isEmpty()) {
                accumulatedPath.append(part).append("/");
                breadcrumbs.add(new BreadcrumbsResponseDto(part, accumulatedPath.toString()));
            }
        }
        return breadcrumbs;
    }
}
