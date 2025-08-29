package com.example.placement_portal.service;

import com.example.placement_portal.entity.AIShortlist;
import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Student;
import java.util.List;
import java.util.Optional;

public interface AIShortlistService {
    AIShortlist saveAIShortlist(AIShortlist aiShortlist);
    AIShortlist updateAIShortlist(AIShortlist aiShortlist);
    Optional<AIShortlist> findById(Long id);
    List<AIShortlist> findAllAIShortlists();
    List<AIShortlist> findByJob(Job job);
    List<AIShortlist> findByJobId(Long jobId);
    List<AIShortlist> findByStudent(Student student);
    List<AIShortlist> findByStudentId(Long studentId);
    List<AIShortlist> findByJobOrderByRank(Job job);
    List<AIShortlist> findTopRankedByJob(Job job, int limit);
    void generateAIShortlist(Long jobId);
    void deleteAIShortlist(Long id);
    void deleteByJobId(Long jobId);
}