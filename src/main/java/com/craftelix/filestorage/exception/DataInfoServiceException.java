package com.craftelix.filestorage.exception;

public class DataInfoServiceException extends RuntimeException {

    public DataInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInfoServiceException(String message) {
        super(message);
    }
}
