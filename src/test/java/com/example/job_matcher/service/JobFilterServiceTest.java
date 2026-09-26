package com.example.job_matcher.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.example.job_matcher.model.Job;
import com.example.job_matcher.model.JobSearchProfile;

public class JobFilterServiceTest {

    
    private JobFilterService jobFilterService = new JobFilterService();

    @Test
    void shouldRemoveJobWithExcludedTitleKeyword() {
        JobSearchProfile profile = mock(JobSearchProfile.class);

        when(profile.excludedSkills()).thenReturn(List.of());
        when(profile.excludedTitleKeywords())
                .thenReturn(List.of("expert", "expérimenté"));

        Job expertJob = new Job(
                "1",
                "Expert JAVA",
                "Test Company",
                "France",
                "Java Spring Boot",
                "FULL_TIME",
                null,
                null,
                false,
                "https://example.com"
        );

        Job normalJob = new Job(
                "2",
                "Développeur Java Junior",
                "Test Company",
                "France",
                "Java Spring Boot",
                "FULL_TIME",
                null,
                null,
                false,
                "https://example.com"
        );

        List<Job> result = jobFilterService.filter(
                List.of(expertJob, normalJob),
                profile
        );

        assertEquals(1, result.size());
        assertEquals(normalJob, result.getFirst());
    }
}
