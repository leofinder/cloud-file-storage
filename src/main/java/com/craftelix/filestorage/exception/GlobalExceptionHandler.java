package com.craftelix.filestorage.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRoleNotFoundExceptions(final RoleNotFoundException ex) {
        log.error(ex.getMessage());
        return "/errors/500";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundExceptions(final Exception ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return "/errors/404";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeExceptions(final Exception ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return "/errors/500";
    }

}
