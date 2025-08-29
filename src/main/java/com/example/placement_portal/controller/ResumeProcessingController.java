package com.example.placement_portal.controller;

import com.example.placement_portal.service.ResumeProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeProcessingController {

    @Autowired
    private ResumeProcessingService resumeProcessingService;

    @PostMapping("/upload/{studentId}")
    public ResponseEntity<String> uploadResume(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {
        try {
            String resumeUrl = resumeProcessingService.uploadResume(studentId, file);
            return ResponseEntity.ok("Resume uploaded successfully. URL: " + resumeUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading resume: " + e.getMessage());
        }
    }

    @GetMapping("/extract-skills")
    public ResponseEntity<String> extractSkillsFromResume(@RequestParam String resumeUrl) {
        try {
            String skills = resumeProcessingService.extractSkillsFromResume(resumeUrl);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error extracting skills: " + e.getMessage());
        }
    }

    @PostMapping("/process-ai/{studentId}")
    public ResponseEntity<String> processResumeWithAI(@PathVariable Long studentId) {
        try {
            resumeProcessingService.processResumeWithAI(studentId);
            return ResponseEntity.ok("Resume processed with AI successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing resume with AI: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateResumeFormat(@RequestParam("file") MultipartFile file) {
        boolean isValid = resumeProcessingService.validateResumeFormat(file);
        return ResponseEntity.ok(isValid);
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<String> deleteResume(@PathVariable Long studentId) {
        try {
            resumeProcessingService.deleteResume(studentId);
            return ResponseEntity.ok("Resume deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting resume: " + e.getMessage());
        }
    }
}