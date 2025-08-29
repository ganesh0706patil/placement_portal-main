package com.example.placement_portal.service;

import com.example.placement_portal.entity.Job;
import java.util.Map;

public interface MLCommunicationService {
    Map<String, Object> matchResumeToJob(String resumeUrl, Job job);
    String extractSkillsFromResume(String resumeUrl);
}