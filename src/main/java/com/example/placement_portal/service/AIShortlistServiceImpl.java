package com.example.placement_portal.service;

import com.example.placement_portal.entity.AIShortlist;
import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.Application;
import com.example.placement_portal.entity.ApplicationStatus;
import com.example.placement_portal.repo.AIShortlistRepo;
import com.example.placement_portal.repo.JobRepo;
import com.example.placement_portal.repo.ApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AIShortlistServiceImpl implements AIShortlistService {

    @Autowired
    private AIShortlistRepo aiShortlistRepo;

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private ApplicationRepo applicationRepo;
    
    @Autowired
    private MLCommunicationService mlCommunicationService;

    @Override
    public AIShortlist saveAIShortlist(AIShortlist aiShortlist) {
        return aiShortlistRepo.save(aiShortlist);
    }

    @Override
    public AIShortlist updateAIShortlist(AIShortlist aiShortlist) {
        return aiShortlistRepo.save(aiShortlist);
    }

    @Override
    public Optional<AIShortlist> findById(Long id) {
        return aiShortlistRepo.findById(id);
    }

    @Override
    public List<AIShortlist> findAllAIShortlists() {
        return aiShortlistRepo.findAll();
    }

    @Override
    public List<AIShortlist> findByJob(Job job) {
        return aiShortlistRepo.findByJob(job);
    }

    @Override
    public List<AIShortlist> findByJobId(Long jobId) {
        return aiShortlistRepo.findByJobId(jobId);
    }

    @Override
    public List<AIShortlist> findByStudent(Student student) {
        return aiShortlistRepo.findByStudent(student);
    }

    @Override
    public List<AIShortlist> findByStudentId(Long studentId) {
        return aiShortlistRepo.findByStudentId(studentId);
    }

    @Override
    public List<AIShortlist> findByJobOrderByRank(Job job) {
        return aiShortlistRepo.findByJobOrderByRankAsc(job);
    }

    @Override
    public List<AIShortlist> findTopRankedByJob(Job job, int limit) {
        return aiShortlistRepo.findTopByJobOrderByRankAsc(job, limit);
    }

    @Override
    public void generateAIShortlist(Long jobId) {
        Optional<Job> jobOpt = jobRepo.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            
            // Get all applications for this job
            List<Application> applications = applicationRepo.findByJobAndStatus(job, ApplicationStatus.APPLIED);
            
            // Clear existing AI shortlist for this job
            aiShortlistRepo.deleteByJobId(jobId);
            
            // Generate AI scores and rankings using ML service
            List<AIShortlist> shortlists = applications.stream()
                .filter(app -> app.getStudent().getResumeUrl() != null) // Only process students with resumes
                .map(app -> {
                    AIShortlist shortlist = new AIShortlist();
                    shortlist.setJob(job);
                    shortlist.setStudent(app.getStudent());
                    
                    // Use ML service to calculate score
                    Double score = calculateAIScore(app.getStudent(), job);
                    shortlist.setScore(score);
                    
                    return shortlist;
                })
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());
            
            // Assign ranks
            for (int i = 0; i < shortlists.size(); i++) {
                shortlists.get(i).setRank(i + 1);
            }
            
            // Save all shortlists
            aiShortlistRepo.saveAll(shortlists);
        }
    }

    private Double calculateAIScore(Student student, Job job) {
        if (student.getResumeUrl() == null) {
            // Fallback to CGPA-based scoring if no resume
            return calculateCgpaBasedScore(student);
        }
        
        try {
            // Use ML service to match resume to job
            Map<String, Object> result = mlCommunicationService.matchResumeToJob(
                student.getResumeUrl(), job);
            
            if (result != null && result.containsKey("score")) {
                return ((Number) result.get("score")).doubleValue();
            }
            
            // Fallback to CGPA-based scoring if ML service fails
            return calculateCgpaBasedScore(student);
        } catch (Exception e) {
            // Log the error and fallback to CGPA-based scoring
            System.err.println("Error calculating AI score: " + e.getMessage());
            return calculateCgpaBasedScore(student);
        }
    }
    
    private Double calculateCgpaBasedScore(Student student) {
        // Simplified scoring based on CGPA (40% weight)
        double score = 0.0;
        
        if (student.getCgpa() != null) {
            score += (student.getCgpa() / 10.0) * 40;
        }
        
        // Add a base score for other factors
        score += 30;
        
        return Math.min(score, 100.0); // Cap at 100
    }

    @Override
    public void deleteAIShortlist(Long id) {
        aiShortlistRepo.deleteById(id);
    }

    @Override
    public void deleteByJobId(Long jobId) {
        aiShortlistRepo.deleteByJobId(jobId);
    }
}