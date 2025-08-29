package com.example.placement_portal.repo;

import com.example.placement_portal.entity.Application;
import com.example.placement_portal.entity.ApplicationStatus;
import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {

    // Find applications by student
    List<Application> findByStudent(Student student);
    List<Application> findByStudentId(Long studentId);

    // Find applications by job
    List<Application> findByJob(Job job);
    List<Application> findByJobId(Long jobId);

    // Find applications by status
    List<Application> findByStatus(ApplicationStatus status);

    // Find applications by student and status
    List<Application> findByStudentAndStatus(Student student, ApplicationStatus status);

    // Find applications by job and status
    List<Application> findByJobAndStatus(Job job, ApplicationStatus status);

    // Check if student already applied for a job
    Optional<Application> findByStudentAndJob(Student student, Job job);
    boolean existsByStudentAndJob(Student student, Job job);

    // Find applications in date range
    List<Application> findByAppliedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find recent applications (last 7 days)
    @Query("SELECT a FROM Application a WHERE a.appliedAt >= :weekAgo ORDER BY a.appliedAt DESC")
    List<Application> findRecentApplications(@Param("weekAgo") LocalDateTime weekAgo);

    // Count applications by status for a job
    @Query("SELECT a.status, COUNT(a) FROM Application a WHERE a.job = :job GROUP BY a.status")
    List<Object[]> countApplicationsByStatusForJob(@Param("job") Job job);

    // Count applications by student
    @Query("SELECT COUNT(a) FROM Application a WHERE a.student = :student")
    Long countApplicationsByStudent(@Param("student") Student student);

    // Find applications by company (through job)
    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId")
    List<Application> findApplicationsByCompanyId(@Param("companyId") Long companyId);

    // Find successful applications (selected students)
    @Query("SELECT a FROM Application a WHERE a.status = 'SELECTED'")
    List<Application> findSelectedApplications();

    // Get application statistics for a job
    @Query("SELECT COUNT(a), " +
            "SUM(CASE WHEN a.status = 'APPLIED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'SHORTLISTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'SELECTED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END) " +
            "FROM Application a WHERE a.job = :job")
    Object[] getApplicationStatsForJob(@Param("job") Job job);
}
