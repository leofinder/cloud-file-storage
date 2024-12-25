package com.craftelix.filestorage.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class FileSizeFormatter {

    public static String formatSize(Long size) {
        DecimalFormat df = new DecimalFormat("#.#");

        if (size == null) {
            return "-   ";
        } else if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return df.format(size / 1024.0) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return df.format(size / (1024.0 * 1024.0)) + " MB";
        } else {
            return df.format(size / (1024.0 * 1024.0 * 1024.0)) + " GB";
        }
    }
}
