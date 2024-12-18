package com.craftelix.filestorage.exception;

public class DataInfoAlreadyExistException extends RuntimeException {

    public DataInfoAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInfoAlreadyExistException(String message) {
        super(message);
    }
}
