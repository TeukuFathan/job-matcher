package com.example.job_matcher.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.job_matcher.dto.JSearchJobDto;
import com.example.job_matcher.dto.JSearchResponse;
import com.example.job_matcher.model.Job;

@Service
public class JSearchService {

        // RestClient is what we use to make HTTP requests to JSearch.
        private final RestClient restClient;

        public JSearchService(
            @Value("${jsearch.api.base-url}") String baseUrl,
            @Value("${jsearch.api.key}") String apiKey) {

        // Here we configure the client once when the application starts.
        // The base URL is the main JSearch address.
        // The API key is added to every request so JSearch can authorize us.
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x-api-key", apiKey)
                .build();
        }

        // This method searches for jobs using the query we receive.
        // For now, we just return the full JSON response as a String.
        public List<Job> searchJobs(String query) {
        JSearchResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/jsearch/search-v2")
                        .queryParam("query", query)
                        .queryParam("country", "fr")
                        .queryParam("language", "fr")
                        .build())
                .retrieve()
                .body(JSearchResponse.class);

        if (response == null
                || response.data() == null
                || response.data().jobs() == null) {
                return List.of();
        }

        return response.data()
                .jobs()
                .stream()
                .map(this::toJob)
                .toList();
        }
        

        private Job toJob(JSearchJobDto dto) {
        return new Job(
                dto.id(),
                dto.title(),
                dto.company(),
                dto.city() + ", " + dto.country(),
                dto.description(),
                dto.employmentType(),
                dto.salaryMin(),
                dto.salaryMax(),
                Boolean.TRUE.equals(dto.remote()),
                dto.applyUrl()
        );
        }
}