package com.example.job_matcher.model;

import java.util.List;

public record JobSearchProfile(
    List<String> targetRoles,
    List<String> skills,
    List<String> locations,
    boolean remoteAccepted,
    Double minimumSalary,
    List<String> excludedSkills,
    List<String> excludedTitleKeywords
) {}