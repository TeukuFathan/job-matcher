package com.example.job_matcher.exception;

public class InvalidSearchQueryException extends RuntimeException {

    public InvalidSearchQueryException(String message) {
        super(message);
    }
}