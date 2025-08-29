package com.example.placement_portal.service;

import com.example.placement_portal.entity.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class MLCommunicationServiceImpl implements MLCommunicationService {

    private final RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    public MLCommunicationServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Map<String, Object> matchResumeToJob(String resumeUrl, Job job) {
        String url = mlServiceUrl + "/api/match";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("resume_path", resumeUrl);
        requestBody.put("job_description", job.getDescription());
        requestBody.put("required_skills", job.getRequiredSkills());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.postForObject(url, entity, Map.class);
        } catch (Exception e) {
            // Log the exception properly in a real application
            System.err.println("Error calling ML service: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String extractSkillsFromResume(String resumeUrl) {
        String url = mlServiceUrl + "/api/extract-skills";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("resume_path", resumeUrl);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);
            if (response != null && response.containsKey("keywords")) {
                return String.join(", ", (java.util.List<String>) response.get("keywords"));
            }
            return "";
        } catch (Exception e) {
            System.err.println("Error calling ML service for skill extraction: " + e.getMessage());
            return null;
        }
    }
}