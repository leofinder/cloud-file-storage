package com.craftelix.filestorage.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class PathUtil {

    public static String getMinioPath(String path, Long userId) {
        return String.format("user-%d-files%s", userId, path);
    }

    public static String getFullPath(String parentPath, String name, boolean isFolder) {
        String pathPostfix = isFolder ? "/" : "";
        return parentPath.endsWith("/") ? parentPath + name + pathPostfix : parentPath + "/" + name + pathPostfix;
    }

    public static List<String> splitPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        return Arrays.asList(path.split("/"));
    }

    public static String getFilename(String path) {
        List<String> names = splitPath(path);
        return names.get(names.size() - 1);
    }

    public static String getParentPath(String path) {
        List<String> names = splitPath(path);

        if (names.size() <= 1 || names.size() == 2 && names.get(0).isEmpty()) {
            return "/";
        }

        String combinedPath = names.stream()
                .limit(names.size() - 1)
                .collect(Collectors.joining("/"));
        return combinedPath + "/";
    }
}
