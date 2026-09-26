package com.example.job_matcher.service;

import com.example.job_matcher.model.JobSearchProfile;
import tools.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JobSearchProfileService {

    private final JobSearchProfile profile;

    public JobSearchProfileService(ObjectMapper objectMapper) {
        try {
            var resource =
                    new ClassPathResource("config/job-search-profile.json");

            this.profile = objectMapper.readValue(
                    resource.getInputStream(),
                    JobSearchProfile.class
            );
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not load job search profile",
                    exception
            );
        }
    }

    public JobSearchProfile getProfile() {
        return profile;
    }
}