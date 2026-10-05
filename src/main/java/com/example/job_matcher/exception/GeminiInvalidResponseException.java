package com.example.job_matcher.exception;

public class GeminiInvalidResponseException extends RuntimeException {

    public GeminiInvalidResponseException(String message) {
        super(message);
    }

    public GeminiInvalidResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}