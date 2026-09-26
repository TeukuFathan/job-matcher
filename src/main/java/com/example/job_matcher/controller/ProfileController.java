package com.example.job_matcher.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.job_matcher.model.JobSearchProfile;
import com.example.job_matcher.service.JobSearchProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final JobSearchProfileService profileService;

    public ProfileController(JobSearchProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public JobSearchProfile getProfile() {
        return profileService.getProfile();
    }
}