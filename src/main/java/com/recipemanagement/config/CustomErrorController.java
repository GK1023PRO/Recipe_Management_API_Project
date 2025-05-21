package com.recipemanagement.config;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom error handler for the application.
 * Provides user-friendly error responses for both API and browser requests.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles error requests and returns appropriate responses
     * based on the Accept header (HTML or JSON).
     *
     * @param request The HTTP request that triggered the error
     * @return Error response (HTML redirect or JSON)
     */
    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorPath = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // If the error is related to static resources, redirect to home
        if (errorPath != null && (
                errorPath.contains("/OOPDocumentationJavaDoc") ||
                        errorPath.equals("/") ||
                        errorPath.equals(""))) {
            return ResponseEntity
                    .status(HttpStatus.SEE_OTHER)
                    .header("Location", "/OOPDocumentationJavaDoc/index.html")
                    .build();
        }

        // Default to 500 if no status code is available
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        if (status != null) {
            int statusValue = Integer.parseInt(status.toString());
            statusCode = HttpStatus.valueOf(statusValue);
        }

        // Create error response
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", statusCode.value());
        errorDetails.put("error", statusCode.getReasonPhrase());
        errorDetails.put("message", "An error occurred processing your request");
        errorDetails.put("path", request.getRequestURI());

        return ResponseEntity
                .status(statusCode)
                .body(errorDetails);
    }

    /**
     * Provides a separate health endpoint at error path to verify the application is running
     * even when other errors occur.
     *
     * @return Simple health status message
     */
    @GetMapping("/error/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Recipe Management API");
        return ResponseEntity.ok(response);
    }
}