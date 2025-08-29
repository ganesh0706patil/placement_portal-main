package com.example.placement_portal.service;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeProcessingService {
    String uploadResume(Long studentId, MultipartFile file);
    String extractSkillsFromResume(String resumeUrl);
    void processResumeWithAI(Long studentId);
    boolean validateResumeFormat(MultipartFile file);
    void deleteResume(Long studentId);
}