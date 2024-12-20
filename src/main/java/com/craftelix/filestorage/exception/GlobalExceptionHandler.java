package com.craftelix.filestorage.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PathNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePathNotFoundException(PathNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return "/error/404";
    }

    @ExceptionHandler(MinioObjectNotFoundException.class)
    public String handleMinioObjectNotFoundException(MinioObjectNotFoundException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(MinioObjectAlreadyExistsException.class)
    public String handleMinioObjectAlreadyExistsException(MinioObjectAlreadyExistsException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(MinioServiceException.class)
    public String handleMinioServiceException(MinioServiceException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error("Minio Exception: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", "An error occurred on the server. Please try again later.");
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(DataInfoServiceException.class)
    public String handleDataInfoServiceException(DataInfoServiceException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error("Data Info Exception: {}", ex.getMessage(), ex);
        String warningMessage = "The operation with files was successfully completed, but there was a problem updating metadata. Please contact support.";
        redirectAttributes.addFlashAttribute("warningMessage", warningMessage);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeExceptions(RuntimeException ex) {
        log.error("HTTP Response Code: {}. {}", HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return "/error/500";
    }

}
