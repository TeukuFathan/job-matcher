package com.example.job_matcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeminiQuotaExceededException.class)
    public ResponseEntity<Map<String, String>> handleGeminiQuotaExceeded(
            GeminiQuotaExceededException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of(
                        "error", "Gemini quota exceeded. Please try again later."
                ));
    }
    // Handles failures when calling the external JSearch API
    // i.e : invalid API key, server error, or connection failure
    @ExceptionHandler(JSearchApiException.class)
    public ResponseEntity<Map<String, String>> handleJSearchApiException(
            JSearchApiException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "error", "JSearch service is currently unavailable. Please try again later."
                ));
    }

    // Handles invalid search input, such as an empty or blank query.
    @ExceptionHandler(InvalidSearchQueryException.class)
    public ResponseEntity<Map<String, String>> handleInvalidSearchQuery(
            InvalidSearchQueryException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", exception.getMessage()
                ));
    }

    @ExceptionHandler(GeminiInvalidResponseException.class)
    public ResponseEntity<Map<String, String>> handleGeminiInvalidResponse(
            GeminiInvalidResponseException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(Map.of(
                        "error", "Gemini returned an invalid response. Please try again later."
                ));
    }
    
    // Handles unexpected application errors that do not have a more specific handler.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedError(
            RuntimeException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "An unexpected error occurred."
                ));
    }

    
    
}