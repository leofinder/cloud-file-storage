package com.craftelix.filestorage.exception;

public class MinioObjectNotFoundException extends RuntimeException {

    public MinioObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioObjectNotFoundException(String message) {
        super(message);
    }
}
