package com.craftelix.filestorage.exception;

public class MinioObjectAlreadyExistsException extends RuntimeException {

    public MinioObjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioObjectAlreadyExistsException(String message) {
        super(message);
    }
}
