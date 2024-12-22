package com.craftelix.filestorage.exception;

public class MinioPathNotFoundException extends RuntimeException {

    public MinioPathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioPathNotFoundException(String message) {
        super(message);
    }
}
