package com.example.job_matcher.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.example.job_matcher.exception.GeminiInvalidResponseException;
import com.example.job_matcher.client.GeminiClient;
import com.example.job_matcher.dto.JobMatchResult;
import com.example.job_matcher.model.Job;
import tools.jackson.databind.ObjectMapper;

@Service 
public class JobMatchingService {
    
    
        private final GeminiClient geminiClient;
        private final String promptString;
        private final ObjectMapper objectMapper;

        
        public JobMatchingService(
                GeminiClient geminiClient,         
                @Value("classpath:prompts/job-matching.txt") Resource promptFile, 
                ObjectMapper objectMapper) throws IOException {

                this.geminiClient = geminiClient;
                this.promptString = promptFile.getContentAsString(StandardCharsets.UTF_8);
                this.objectMapper = objectMapper;

        }
        /**
         * Evaluates one job using Gemini and returns the structured match result.
         *
         * @param job the job to evaluate
         * @return Gemini's structured evaluation of the job
         */
        public JobMatchResult matchJobs(Job job) {
        String prompt = promptString
                + "\n\nTitre :\n" + job.title()
                + "\n\nDescription :\n" + job.description();

        String json = geminiClient.generateJson(
                prompt,
                "gemini-3.5-flash-lite",
                RESULT_SCHEMA
        );

        return parseResult(json);
        }

        
        private JobMatchResult parseResult(String json) {
        if (json == null || json.isBlank()) {
                throw new GeminiInvalidResponseException("Gemini returned an empty response");
        }

        JobMatchResult result;

        try {
                result = objectMapper.readValue(json, JobMatchResult.class);
        } catch (Exception exception) {
                throw new GeminiInvalidResponseException(
                        "Could not parse Gemini's response",
                        exception
                );
        }

        if (result == null
                || result.decision() == null
                || result.experienceReason() == null
                || result.matchedTechnologies() == null
                || result.summary() == null) {
                throw new GeminiInvalidResponseException("Gemini returned an incomplete result");
        }

        return result;
        }


        private static final Map<String, Object> RESULT_SCHEMA = Map.of(
                "type", "object",
                "properties", Map.of(
                        "decision", Map.of(
                                "type", "string",
                                "enum", List.of("MATCH", "NO_MATCH", "UNCERTAIN")
                        ),
                        "experienceReason", Map.of("type", "string"),
                        "matchedTechnologies", Map.of(
                                "type", "array",
                                "items", Map.of("type", "string")
                        ),
                        "summary", Map.of("type", "string")
                ),
                "required", List.of(
                        "decision",
                        "experienceReason",
                        "matchedTechnologies",
                        "summary"
                )
        );
}
