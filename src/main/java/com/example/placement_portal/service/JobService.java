package com.example.placement_portal.service;

import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Company;
import java.util.List;
import java.util.Optional;

public interface JobService {
    Job saveJob(Job job);
    Job updateJob(Job job);
    Optional<Job> findById(Long id);
    List<Job> findAllJobs();
    List<Job> findActiveJobs();
    List<Job> findInactiveJobs();
    List<Job> findByCompany(Company company);
    List<Job> findByCompanyId(Long companyId);
    List<Job> findActiveJobsByCompany(Company company);
    List<Job> findOpenJobsForRegistration();
    List<Job> findJobsByPackageRange(Double minPackage, Double maxPackage);
    List<Job> findJobsByMinPackage(Double minPackage);
    List<Job> findJobsByMaxPackage(Double maxPackage);
    List<Job> findJobsByMinCgpa(Double studentCgpa);
    List<Job> findJobsByTitle(String title);
    List<Job> findSuitableJobsForStudent(Double studentCgpa);
    List<Object[]> getActiveJobCountByCompany();
    Job activateJob(Long jobId);
    Job deactivateJob(Long jobId);
    void deleteJob(Long id);
}
