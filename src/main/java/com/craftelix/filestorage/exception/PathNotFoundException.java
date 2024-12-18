package com.craftelix.filestorage.exception;

public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathNotFoundException(String message) {
        super(message);
    }
}
