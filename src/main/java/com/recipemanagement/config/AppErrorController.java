package com.recipemanagement.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Error controller for handling application-wide error responses.
 *
 * <p>This controller implements Spring Boot's {@link ErrorController} interface
 * to provide a custom error handling endpoint that returns a user-friendly message
 * when errors occur within the application.</p>
 *
 * @author Georges Khoury
 * @version 1.1
 * @since 1.0
 */
@RestController
public class AppErrorController implements ErrorController {

    /**
     * Handles all error requests forwarded to the /error path.
     *
     * <p>This method returns a simple message to the client when any error occurs
     * within the application and the request is forwarded to the /error endpoint.</p>
     *
     * @param request The current HTTP request
     * @return A response entity with error details
     */
    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        // Get the status code from the request
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        // Create a simple error response
        Map<String, Object> body = new HashMap<>();
        body.put("status", statusCode);
        body.put("message", "Something went wrong. Please try again later.");

        return new ResponseEntity<>(body, HttpStatus.valueOf(statusCode));
    }
}