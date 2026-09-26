package com.example.job_matcher.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

import org.springframework.stereotype.Component;

@Component
public class GeminiClient {

    private final Client client;

    public GeminiClient(@Value("${gemini.api.key}") String apiKey) {
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String generate(String prompt, String model) {
        GenerateContentResponse response =
                //.generateContent(model, prompt, null); are used to send http request to gemini api and get the response
                client.models.generateContent(model, prompt, null);

        return response.text();
    }

    public String generateJson(
        String prompt,
        String model,
        Map<String, Object> schema
    ) {
        GenerateContentConfig config = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .responseJsonSchema(schema)
                .build();

        return client.models.generateContent(model, prompt, config).text();
    }
}