package com.example.placement_portal.service;

import com.example.placement_portal.entity.*;
import java.util.List;
import java.util.Map;

public interface PlacementService {
    Map<String, Object> getDashboardStats();
    Map<String, Object> getStudentDashboard(Long studentId);
    Map<String, Object> getCompanyDashboard(Long companyId);
    void processJobApplications(Long jobId);
    void generateAndUpdateShortlists(Long jobId);
    List<Student> getShortlistedStudents(Long jobId);
    List<Object[]> getPlacementStatistics();
    List<Object[]> getCompanyWisePlacementStats();
    List<Object[]> getBranchWiseePlacementStats();
    List<Student> getStudentsEligibleForJob(Long jobId);
    void notifyStudentsAboutNewJob(Long jobId);
    void notifyShortlistedStudents(Long jobId);
}