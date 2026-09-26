package com.example.job_matcher.service;

import com.example.job_matcher.dto.JobMatchResult;
import com.example.job_matcher.model.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("local")
class JobMatchingServiceTest {

    @Autowired
    private JobMatchingService jobMatchingService;

    @Test
    void shouldEvaluateJuniorJavaJob() {
        // Arrange: prepare a sample job
        Job job = mock(Job.class);
        when(job.title()).thenReturn("Développeur Java junior");
        when(job.description()).thenReturn(
                "Nous recherchons un développeur Java et Spring Boot "
                + "avec 0 à 2 ans d'expérience."
        );

        JobMatchResult result = jobMatchingService.matchJobs(job);

        assertNotNull(result);
        assertEquals(JobMatchResult.Decision.MATCH, result.decision());
        assertTrue(result.matchedTechnologies().contains("Java"));

        System.out.println(result);
    }
}