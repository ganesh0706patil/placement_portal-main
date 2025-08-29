package com.example.placement_portal.service;

import com.example.placement_portal.entity.Application;
import com.example.placement_portal.entity.ApplicationStatus;
import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.Job;
import com.example.placement_portal.repo.ApplicationRepo;
import com.example.placement_portal.repo.StudentRepo;
import com.example.placement_portal.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private JobRepo jobRepo;

    @Override
    public Application saveApplication(Application application) {
        return applicationRepo.save(application);
    }

    @Override
    public Application updateApplication(Application application) {
        return applicationRepo.save(application);
    }

    @Override
    public Optional<Application> findById(Long id) {
        return applicationRepo.findById(id);
    }

    @Override
    public List<Application> findAllApplications() {
        return applicationRepo.findAll();
    }

    @Override
    public List<Application> findByStudent(Student student) {
        return applicationRepo.findByStudent(student);
    }

    @Override
    public List<Application> findByStudentId(Long studentId) {
        return applicationRepo.findByStudentId(studentId);
    }

    @Override
    public List<Application> findByJob(Job job) {
        return applicationRepo.findByJob(job);
    }

    @Override
    public List<Application> findByJobId(Long jobId) {
        return applicationRepo.findByJobId(jobId);
    }

    @Override
    public List<Application> findByStatus(ApplicationStatus status) {
        return applicationRepo.findByStatus(status);
    }

    @Override
    public List<Application> findByStudentAndStatus(Student student, ApplicationStatus status) {
        return applicationRepo.findByStudentAndStatus(student, status);
    }

    @Override
    public List<Application> findByJobAndStatus(Job job, ApplicationStatus status) {
        return applicationRepo.findByJobAndStatus(job, status);
    }

    @Override
    public Optional<Application> findByStudentAndJob(Student student, Job job) {
        return applicationRepo.findByStudentAndJob(student, job);
    }

    @Override
    public boolean hasStudentApplied(Student student, Job job) {
        return applicationRepo.existsByStudentAndJob(student, job);
    }

    @Override
    public List<Application> findApplicationsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return applicationRepo.findByAppliedAtBetween(startDate, endDate);
    }

    @Override
    public List<Application> findRecentApplications() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return applicationRepo.findRecentApplications(weekAgo);
    }

    @Override
    public List<Object[]> getApplicationStatusCountForJob(Job job) {
        return applicationRepo.countApplicationsByStatusForJob(job);
    }

    @Override
    public Long getApplicationCountByStudent(Student student) {
        return applicationRepo.countApplicationsByStudent(student);
    }

    @Override
    public List<Application> findApplicationsByCompanyId(Long companyId) {
        return applicationRepo.findApplicationsByCompanyId(companyId);
    }

    @Override
    public List<Application> findSelectedApplications() {
        return applicationRepo.findSelectedApplications();
    }

    @Override
    public Object[] getApplicationStatsForJob(Job job) {
        return applicationRepo.getApplicationStatsForJob(job);
    }

    @Override
    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status) {
        Optional<Application> appOpt = applicationRepo.findById(applicationId);
        if (appOpt.isPresent()) {
            Application application = appOpt.get();
            application.setStatus(status);
            return applicationRepo.save(application);
        }
        throw new RuntimeException("Application not found with id: " + applicationId);
    }

    @Override
    public Application applyForJob(Long studentId, Long jobId) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        Optional<Job> jobOpt = jobRepo.findById(jobId);

        if (studentOpt.isPresent() && jobOpt.isPresent()) {
            Student student = studentOpt.get();
            Job job = jobOpt.get();

            // Check if student already applied
            if (applicationRepo.existsByStudentAndJob(student, job)) {
                throw new RuntimeException("Student has already applied for this job");
            }

            // Check eligibility
            if (student.getCgpa() < job.getMinCgpa()) {
                throw new RuntimeException("Student does not meet minimum CGPA requirement");
            }

            // Check if registration is still open
            if (job.getRegistrationEnd().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Registration deadline has passed");
            }

            Application application = new Application();
            application.setStudent(student);
            application.setJob(job);
            application.setStatus(ApplicationStatus.APPLIED);

            return applicationRepo.save(application);
        }
        throw new RuntimeException("Student or Job not found");
    }

    @Override
    public void deleteApplication(Long id) {
        applicationRepo.deleteById(id);
    }
}