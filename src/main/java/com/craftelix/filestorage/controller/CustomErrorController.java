package com.craftelix.filestorage.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");

        if (statusCode != null) {
            log.error("HTTP Response Code: {}, uri: {}, message: {}", statusCode, requestUri, message, exception);
            return switch (statusCode) {
                case 404 -> "error/404";
                case 500 -> "error/500";
                default -> "error/error";
            };
        }

        log.error("Unexpected error. uri: {}, message: {}", requestUri, message, exception);
        return "error/error";
    }
}
