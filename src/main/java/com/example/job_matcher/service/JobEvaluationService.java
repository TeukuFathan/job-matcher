package com.example.job_matcher.service;

import com.example.job_matcher.dto.EvaluatedJob;
import com.example.job_matcher.model.Job;
import com.example.job_matcher.model.JobSearchProfile;
import com.example.job_matcher.dto.SimplifiedJobResultDto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobEvaluationService {

    private final JobFilterService jobFilterService;
    private final JobMatchingService jobMatchingService;
    private final JSearchService jSearchService;

    private static final int MAX_JOBS_TO_EVALUATE = 5;


    public JobEvaluationService(
            JobFilterService jobFilterService,
            JobMatchingService jobMatchingService,
            JSearchService jSearchService
    ) {
        this.jobFilterService = jobFilterService;
        this.jobMatchingService = jobMatchingService;
        this.jSearchService = jSearchService;
    }

    public List<SimplifiedJobResultDto> evaluate(List<Job> jobs, JobSearchProfile profile) {

        List<Job> filteredJobs = jobFilterService.filter(jobs, profile);

        return filteredJobs.stream()
                .limit(MAX_JOBS_TO_EVALUATE)
                .map(job -> new EvaluatedJob(
                        job,
                        jobMatchingService.matchJobs(job)
                ))
                .map(this::toSimplifiedJobResultDto)
                .toList();
    }


    public List<SimplifiedJobResultDto> searchAndEvaluate(String query,JobSearchProfile profile) {    
        List<Job> jobs = jSearchService.searchJobs(query);
        return evaluate(jobs, profile);
    }

    private SimplifiedJobResultDto toSimplifiedJobResultDto(EvaluatedJob evaluatedJob) {
        Job job = evaluatedJob.job();

        return new SimplifiedJobResultDto(
                job.title(),
                job.company(),
                job.applyUrl()
    );
}
}