package com.example.job_matcher.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.job_matcher.model.Job;
import com.example.job_matcher.model.JobSearchProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import com.example.job_matcher.dto.EvaluatedJob;
import com.example.job_matcher.dto.JobMatchResult;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobEvaluationServiceTest {

    @Mock
    private JSearchService jSearchService;

    @Mock
    private JobFilterService jobFilterService;

    @Mock
    private JobMatchingService jobMatchingService;

    @InjectMocks
    private JobEvaluationService jobEvaluationService;

    private Job javaJob;
    private Job pythonJob;
    private JobSearchProfile profile;

    @BeforeEach
    void setUp() {
        javaJob = new Job(
                "1",
                "Junior Java Developer",
                "Test Company",
                "Toulouse",
                "Java Spring Boot",
                "FULL_TIME",
                null,
                null,
                false,
                "https://example.com/java"
        );

        pythonJob = new Job(
                "2",
                "Python Developer",
                "Test Company",
                "Toulouse",
                "Python Django",
                "FULL_TIME",
                null,
                null,
                false,
                "https://example.com/python"
        );

        profile = mock(JobSearchProfile.class);

    }

    @Test
    void shouldFilterJobsAndEvaluateRemainingJobs() {


        JobMatchResult matchResult = new JobMatchResult(
                JobMatchResult.Decision.MATCH,
                "Junior position",
                List.of("Java", "Spring Boot"),
                "Good match"
        );

        List<Job> jobs = List.of(javaJob, pythonJob);

        when(jobFilterService.filter(jobs, profile))
                .thenReturn(List.of(javaJob));

        when(jobMatchingService.matchJobs(javaJob))
                .thenReturn(matchResult);

        List<EvaluatedJob> results =
                jobEvaluationService.evaluate(jobs, profile);

        assertEquals(1, results.size());
        assertEquals(javaJob, results.getFirst().job());
        assertEquals(matchResult, results.getFirst().jobMatchResult());

        verify(jobMatchingService).matchJobs(javaJob);
        verify(jobMatchingService, never()).matchJobs(pythonJob);
    }

    @Test
    void shouldSearchThenFilterAndEvaluateJobs() {

        List<Job> jobs = List.of(javaJob, pythonJob);

        JobMatchResult matchResult = new JobMatchResult(
                JobMatchResult.Decision.MATCH,
                "Junior position",
                List.of("Java", "Spring Boot"),
                "Good match"
        );

        when(jSearchService.searchJobs("java"))
                .thenReturn(jobs);

        when(jobFilterService.filter(jobs, profile))
                .thenReturn(List.of(javaJob));

        when(jobMatchingService.matchJobs(javaJob))
                .thenReturn(matchResult);

        List<EvaluatedJob> results =
                jobEvaluationService.searchAndEvaluate("java", profile);

        assertEquals(1, results.size());

        verify(jSearchService).searchJobs("java");
        verify(jobFilterService).filter(jobs, profile);
        verify(jobMatchingService).matchJobs(javaJob);
    }


    @Test
    void evaluateLimitsGeminiEvaluationToFiveJobs() {
        Job job1 = new Job("1", "Java Developer 1", "Company", "France",
                "Java job", null, null, null, false, null);

        Job job2 = new Job("2", "Java Developer 2", "Company", "France",
                "Java job", null, null, null, false, null);

        Job job3 = new Job("3", "Java Developer 3", "Company", "France",
                "Java job", null, null, null, false, null);

        Job job4 = new Job("4", "Java Developer 4", "Company", "France",
                "Java job", null, null, null, false, null);

        Job job5 = new Job("5", "Java Developer 5", "Company", "France",
                "Java job", null, null, null, false, null);

        Job job6 = new Job("6", "Java Developer 6", "Company", "France",
                "Java job", null, null, null, false, null);

        List<Job> jobs = List.of(
                job1, job2, job3, job4, job5, job6
        );

        when(jobFilterService.filter(jobs, profile))
                .thenReturn(jobs);

        List<EvaluatedJob> result =
                jobEvaluationService.evaluate(jobs, profile);

        assertEquals(5, result.size());

        verify(jobMatchingService, times(5))
                .matchJobs(any(Job.class));

        verify(jobMatchingService, never())
                .matchJobs(job6);
    }
}