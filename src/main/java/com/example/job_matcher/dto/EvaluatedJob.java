package com.example.job_matcher.dto;
import com.example.job_matcher.model.Job;

/**
 * Job + JobMatchResult (from gemini)
 * the original {@link Job} together with its Gemini {@link JobMatchResult}.
 */
public record EvaluatedJob(
        Job job,
        JobMatchResult jobMatchResult
) {
    
}
