package com.example.job_matcher.model;

public record Job(
    String id,
    String title,
    String company,
    String location,
    String description,
    String employmentType,
    Double salaryMin,
    Double salaryMax,
    boolean remote,
    String applyUrl
) {}