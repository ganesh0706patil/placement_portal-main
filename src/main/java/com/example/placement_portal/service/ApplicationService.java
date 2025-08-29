package com.example.placement_portal.service;

import com.example.placement_portal.entity.Application;
import com.example.placement_portal.entity.ApplicationStatus;
import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.Job;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application saveApplication(Application application);
    Application updateApplication(Application application);
    Optional<Application> findById(Long id);
    List<Application> findAllApplications();
    List<Application> findByStudent(Student student);
    List<Application> findByStudentId(Long studentId);
    List<Application> findByJob(Job job);
    List<Application> findByJobId(Long jobId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByStudentAndStatus(Student student, ApplicationStatus status);
    List<Application> findByJobAndStatus(Job job, ApplicationStatus status);
    Optional<Application> findByStudentAndJob(Student student, Job job);
    boolean hasStudentApplied(Student student, Job job);
    List<Application> findApplicationsInDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Application> findRecentApplications();
    List<Object[]> getApplicationStatusCountForJob(Job job);
    Long getApplicationCountByStudent(Student student);
    List<Application> findApplicationsByCompanyId(Long companyId);
    List<Application> findSelectedApplications();
    Object[] getApplicationStatsForJob(Job job);
    Application updateApplicationStatus(Long applicationId, ApplicationStatus status);
    Application applyForJob(Long studentId, Long jobId);
    void deleteApplication(Long id);
}