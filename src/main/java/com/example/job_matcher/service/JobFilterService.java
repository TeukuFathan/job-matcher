package com.example.job_matcher.service;

import com.example.job_matcher.model.Job;
import com.example.job_matcher.model.JobSearchProfile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class JobFilterService {

    public List<Job> filter(
            List<Job> jobs,
            JobSearchProfile profile
    ) {
        return jobs.stream()
                .filter(job -> hasDescription(job))
                .filter(job -> !containsExcludedSkill(job, profile))
                .filter(job -> !containsExcludedTitleKeyword(job, profile))
                //.filter(job -> matchesMinimumSalary(job, profile))
                .toList();
    }

    private boolean hasDescription(Job job) {
        return job.description() != null
                && !job.description().isBlank();
    }

    private boolean containsExcludedSkill(
            Job job,
            JobSearchProfile profile
    ) {
        String searchableText = (
                safe(job.title()) + " " +
                safe(job.description())
        ).toLowerCase(Locale.ROOT);

        return profile.excludedSkills().stream()
                .map(skill -> skill.toLowerCase(Locale.ROOT))
                .anyMatch(searchableText::contains);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private boolean containsExcludedTitleKeyword(Job job, JobSearchProfile profile) {
        String title = safe(job.title()).toLowerCase(Locale.ROOT);

        return profile.excludedTitleKeywords().stream()
                .map(keyword -> keyword.toLowerCase(Locale.ROOT))
                .anyMatch(title::contains);
    }
}