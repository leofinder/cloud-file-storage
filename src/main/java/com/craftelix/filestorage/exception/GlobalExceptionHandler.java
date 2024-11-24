package com.craftelix.filestorage.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRoleNotFoundExceptions(final RoleNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return "/error/500";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeExceptions(final Exception ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return "/error/500";
    }

}
