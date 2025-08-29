package com.example.placement_portal.service;

import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Company;
import com.example.placement_portal.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepo jobRepo;

    @Override
    public Job saveJob(Job job) {
        return jobRepo.save(job);
    }

    @Override
    public Job updateJob(Job job) {
        return jobRepo.save(job);
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepo.findById(id);
    }

    @Override
    public List<Job> findAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public List<Job> findActiveJobs() {
        return jobRepo.findByIsActiveTrue();
    }

    @Override
    public List<Job> findInactiveJobs() {
        return jobRepo.findByIsActiveFalse();
    }

    @Override
    public List<Job> findByCompany(Company company) {
        return jobRepo.findByCompany(company);
    }

    @Override
    public List<Job> findByCompanyId(Long companyId) {
        return jobRepo.findByCompanyId(companyId);
    }

    @Override
    public List<Job> findActiveJobsByCompany(Company company) {
        return jobRepo.findByCompanyAndIsActiveTrue(company);
    }

    @Override
    public List<Job> findOpenJobsForRegistration() {
        return jobRepo.findOpenJobsForRegistration(LocalDateTime.now());
    }

    @Override
    public List<Job> findJobsByPackageRange(Double minPackage, Double maxPackage) {
        return jobRepo.findJobsByPackageRange(minPackage, maxPackage);
    }

    @Override
    public List<Job> findJobsByMinPackage(Double minPackage) {
        return jobRepo.findByMinPackageGreaterThanEqual(minPackage);
    }

    @Override
    public List<Job> findJobsByMaxPackage(Double maxPackage) {
        return jobRepo.findByMaxPackageLessThanEqual(maxPackage);
    }

    @Override
    public List<Job> findJobsByMinCgpa(Double studentCgpa) {
        return jobRepo.findByMinCgpaLessThanEqual(studentCgpa);
    }

    @Override
    public List<Job> findJobsByTitle(String title) {
        return jobRepo.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Job> findSuitableJobsForStudent(Double studentCgpa) {
        return jobRepo.findSuitableJobsForStudent(studentCgpa, LocalDateTime.now());
    }

    @Override
    public List<Object[]> getActiveJobCountByCompany() {
        return jobRepo.countActiveJobsByCompany();
    }

    @Override
    public Job activateJob(Long jobId) {
        Optional<Job> jobOpt = jobRepo.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            job.setIsActive(true);
            return jobRepo.save(job);
        }
        throw new RuntimeException("Job not found with id: " + jobId);
    }

    @Override
    public Job deactivateJob(Long jobId) {
        Optional<Job> jobOpt = jobRepo.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            job.setIsActive(false);
            return jobRepo.save(job);
        }
        throw new RuntimeException("Job not found with id: " + jobId);
    }

    @Override
    public void deleteJob(Long id) {
        jobRepo.deleteById(id);
    }
}