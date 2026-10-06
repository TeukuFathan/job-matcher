package com.example.job_matcher.controller;

import com.example.job_matcher.dto.EvaluatedJob;
import com.example.job_matcher.dto.SimplifiedJobResultDto;
import com.example.job_matcher.exception.InvalidSearchQueryException;
import com.example.job_matcher.service.JobEvaluationService;
import com.example.job_matcher.service.JobSearchProfileService;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JSearchController {

    private final JobEvaluationService jobEvaluationService;
    private final JobSearchProfileService profileService;
    
    public JSearchController(
        JobEvaluationService jobEvaluationService,
        JobSearchProfileService profileService
    ) {
        this.jobEvaluationService = jobEvaluationService;
        this.profileService = profileService;
    }
    // Receives the query from /jobs/search?query=...
    // Then gives the query to the service, returns the JSearch response.
    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    
    public List<SimplifiedJobResultDto> searchJobs(@RequestParam String query) {

        if (query.isBlank()) {
            throw new InvalidSearchQueryException(
                    "Search query must not be blank"
            );
        }

        return jobEvaluationService.searchAndEvaluate(
                query,
                profileService.getProfile()
        );
    }
}   