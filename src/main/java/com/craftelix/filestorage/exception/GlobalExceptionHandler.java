package com.craftelix.filestorage.exception;

import com.craftelix.filestorage.config.properties.FileUploadProperties;
import com.craftelix.filestorage.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final FileUploadProperties fileUploadProperties;

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

    @ExceptionHandler(MinioPathNotFoundException.class)
    public String handleMinioPathNotFoundException(MinioPathNotFoundException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid data provided. Please check the input and try again.");
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                       RedirectAttributes redirectAttributes,
                                                       HttpServletRequest request) {
        log.error("{} for userID: {}", ex.getMessage(), userDetails.getId(), ex);
        String errorMessage = String.format(
                """
                An error occurred while uploading files.
                The maximum allowed size for a single file is %s.
                The maximum total size for all files is %s.
                """,
                fileUploadProperties.getMaxFileSize(),
                fileUploadProperties.getMaxRequestSize()
        );
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
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
