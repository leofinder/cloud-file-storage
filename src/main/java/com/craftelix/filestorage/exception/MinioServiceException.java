package com.craftelix.filestorage.exception;

public class MinioServiceException extends RuntimeException {

    public MinioServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioServiceException(String message) {
        super(message);
    }
}
