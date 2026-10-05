package com.example.job_matcher.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void returns429WhenGeminiQuotaIsExceeded() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var response = handler.handleGeminiQuotaExceeded(
                new GeminiQuotaExceededException(
                        "Gemini quota exceeded",
                        new RuntimeException()
                )
        );

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(
                "Gemini quota exceeded. Please try again later.",
                response.getBody().get("error")
        );
    }

    @Test
    void returns502WhenJSearchApiFails() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var response = handler.handleJSearchApiException(
                new JSearchApiException(
                        "JSearch API request failed",
                        new RuntimeException()
                )
        );

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertEquals(
                "JSearch service is currently unavailable. Please try again later.",
                response.getBody().get("error")
        );
    }

    @Test
    void returns400WhenSearchQueryIsInvalid() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var response = handler.handleInvalidSearchQuery(
                new InvalidSearchQueryException(
                        "Search query must not be blank"
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "Search query must not be blank",
                response.getBody().get("error")
        );
    }

    @Test
    void returns500ForUnexpectedRuntimeException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        var response = handler.handleUnexpectedError(
                new RuntimeException("Something broke")
        );

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals(
                "An unexpected error occurred.",
                response.getBody().get("error")
        );
    }
}