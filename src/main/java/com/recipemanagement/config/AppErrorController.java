package com.recipemanagement.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Error controller for handling application-wide error responses.
 *
 * <p>This controller implements Spring Boot's {@link ErrorController} interface
 * to provide a custom error handling endpoint that returns a user-friendly message
 * when errors occur within the application.</p>
 *
 * @author Georges Khoury
 * @version 1.0
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
     * @return A string containing a user-friendly error message
     */
    @RequestMapping("/error")
    public String handleError() {
        return "Something went wrong. Please try again later.";
    }
}