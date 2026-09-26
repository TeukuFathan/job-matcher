package com.example.job_matcher.dto;

import java.util.List;

public record JobMatchResult(
        Decision decision,
        String experienceReason,
        List<String> matchedTechnologies,
        String summary
) {
    public enum Decision {
        MATCH, NO_MATCH, UNCERTAIN
    }
}