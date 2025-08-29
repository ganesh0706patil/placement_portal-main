package com.example.placement_portal.service;

import com.example.placement_portal.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResumeProcessingServiceImpl implements ResumeProcessingService {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private MLCommunicationService mlCommunicationService;

    @Value("${app.resume.upload.dir:./uploads/resumes/}")
    private String uploadDir;

    @Override
    public String uploadResume(Long studentId, MultipartFile file) {
        if (!validateResumeFormat(file)) {
            throw new RuntimeException("Invalid resume format. Only PDF files are allowed.");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Save file
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Update student record
            String resumeUrl = "/uploads/resumes/" + uniqueFilename;
            studentService.updateResume(studentId, resumeUrl);

            // Process resume with AI
            processResumeWithAI(studentId);

            return resumeUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    @Override
    public String extractSkillsFromResume(String resumeUrl) {
        // Use ML service to extract skills
        return mlCommunicationService.extractSkillsFromResume(resumeUrl);
    }

    @Override
    public void processResumeWithAI(Long studentId) {
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            if (student.getResumeUrl() != null) {
                // Extract skills from resume
                String extractedSkills = extractSkillsFromResume(student.getResumeUrl());
                
                // Update student skills
                studentService.updateSkills(studentId, extractedSkills);
            }
        }
    }

    @Override
    public boolean validateResumeFormat(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            return false;
        }

        // Check file extension
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        return ".pdf".equals(extension);
    }

    @Override
    public void deleteResume(Long studentId) {
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            if (student.getResumeUrl() != null) {
                try {
                    // Delete file from filesystem
                    Path filePath = Paths.get("." + student.getResumeUrl());
                    Files.deleteIfExists(filePath);
                    
                    // Update student record
                    studentService.updateResume(studentId, null);
                    studentService.updateSkills(studentId, null);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete resume", e);
                }
            }
        }
    }
}