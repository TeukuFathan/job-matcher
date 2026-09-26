package com.example.job_matcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JSearchJobDto(
    @JsonProperty("job_id")
    String id,
    
    @JsonProperty("job_title")
    String title,

    @JsonProperty("employer_name")
    String company,

    @JsonProperty("job_city")
    String city,

    @JsonProperty("job_country")
    String country,

    @JsonProperty("job_description")
    String description,

    @JsonProperty("job_employment_type")
    String employmentType,

    @JsonProperty("job_min_salary")
    Double salaryMin,

    @JsonProperty("job_max_salary")
    Double salaryMax,

    @JsonProperty("job_is_remote")
    Boolean remote,

    @JsonProperty("job_apply_link")
    String applyUrl
) {}