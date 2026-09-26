package com.example.job_matcher.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.job_matcher.client.GeminiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping("/test/")
public class GeminiTestController {
    private final GeminiClient geminiClient;
    public GeminiTestController(GeminiClient geminiClient){
        this.geminiClient = geminiClient;
    }

    @GetMapping("getTestGemini")
    public String getTestGemini() {
        return  geminiClient.generate("this a test prompt : tell me a recipe of rendang", "gemini-3.5-flash-lite");
    }
    

}
