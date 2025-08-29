package com.example.placement_portal.service;

import com.example.placement_portal.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PlacementServiceImpl implements PlacementService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AIShortlistService aiShortlistService;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalStudents", studentService.findAllStudents().size());
        stats.put("totalCompanies", companyService.findAllCompanies().size());
        stats.put("activeJobs", jobService.findActiveJobs().size());
        stats.put("totalApplications", applicationService.findAllApplications().size());
        stats.put("selectedStudents", applicationService.findSelectedApplications().size());
        stats.put("companiesWithActiveJobs", companyService.findCompaniesWithActiveJobs().size());
        
        // Recent activity
        stats.put("recentApplications", applicationService.findRecentApplications().size());
        
        // Students with resume
        stats.put("studentsWithResume", studentService.findStudentsWithResume().size());
        stats.put("studentsWithoutResume", studentService.findStudentsWithoutResume().size());
        
        return stats;
    }

    @Override
    public Map<String, Object> getStudentDashboard(Long studentId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        Optional<Student> studentOpt = studentService.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            
            // Student applications
            List<Application> applications = applicationService.findByStudent(student);
            dashboard.put("totalApplications", applications.size());
            
            // Applications by status
            Map<ApplicationStatus, Long> statusCount = new HashMap<>();
            applications.forEach(app -> 
                statusCount.merge(app.getStatus(), 1L, Long::sum)
            );
            dashboard.put("applicationsByStatus", statusCount);
            
            // Suitable jobs
            List<Job> suitableJobs = jobService.findSuitableJobsForStudent(student.getCgpa());
            dashboard.put("suitableJobs", suitableJobs.size());
            
            // AI shortlists
            List<AIShortlist> shortlists = aiShortlistService.findByStudent(student);
            dashboard.put("aiShortlists", shortlists.size());
            
            // Resume status
            dashboard.put("hasResume", student.getResumeUrl() != null);
        }
        
        return dashboard;
    }

    @Override
    public Map<String, Object> getCompanyDashboard(Long companyId) {
        Map<String, Object> dashboard = new HashMap<>();
        
        Optional<Company> companyOpt = companyService.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            
            // Company jobs
            List<Job> jobs = jobService.findByCompany(company);
            List<Job> activeJobs = jobService.findActiveJobsByCompany(company);
            
            dashboard.put("totalJobs", jobs.size());
            dashboard.put("activeJobs", activeJobs.size());
            
            // Applications received
            List<Application> allApplications = applicationService.findApplicationsByCompanyId(companyId);
            dashboard.put("totalApplications", allApplications.size());
            
            // Applications by status
            Map<ApplicationStatus, Long> statusCount = new HashMap<>();
            allApplications.forEach(app -> 
                statusCount.merge(app.getStatus(), 1L, Long::sum)
            );
            dashboard.put("applicationsByStatus", statusCount);
            
            // Selected students
            long selectedCount = allApplications.stream()
                .mapToLong(app -> app.getStatus() == ApplicationStatus.SELECTED ? 1 : 0)
                .sum();
            dashboard.put("selectedStudents", selectedCount);
        }
        
        return dashboard;
    }

    @Override
    public void processJobApplications(Long jobId) {
        Optional<Job> jobOpt = jobService.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            
            // Get all applications for this job
            List<Application> applications = applicationService.findByJobAndStatus(job, ApplicationStatus.APPLIED);
            
            // Filter eligible applications based on CGPA and other criteria
            applications.stream()
                .filter(app -> app.getStudent().getCgpa() >= job.getMinCgpa())
                .forEach(app -> {
                    // Additional processing logic can be added here
                    // For now, just ensure they remain in APPLIED status
                });
            
            // Generate AI shortlist
            aiShortlistService.generateAIShortlist(jobId);
        }
    }

    @Override
    public void generateAndUpdateShortlists(Long jobId) {
        // Generate AI shortlist
        aiShortlistService.generateAIShortlist(jobId);
        
        // Update application status for top candidates
        Optional<Job> jobOpt = jobService.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            
            // Get top 50% of AI shortlisted candidates (or configure as needed)
            List<AIShortlist> topCandidates = aiShortlistService.findTopRankedByJob(job, 20);
            
            topCandidates.forEach(shortlist -> {
                Optional<Application> appOpt = applicationService.findByStudentAndJob(
                    shortlist.getStudent(), shortlist.getJob()
                );
                
                if (appOpt.isPresent()) {
                    applicationService.updateApplicationStatus(
                        appOpt.get().getId(), ApplicationStatus.SHORTLISTED
                    );
                }
            });
        }
    }

    @Override
    public List<Student> getShortlistedStudents(Long jobId) {
        Optional<Job> jobOpt = jobService.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            return applicationService.findByJobAndStatus(job, ApplicationStatus.SHORTLISTED)
                .stream()
                .map(Application::getStudent)
                .toList();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Object[]> getPlacementStatistics() {
        // This would return comprehensive placement statistics
        // Implementation depends on specific reporting requirements
        List<Object[]> stats = new ArrayList<>();
        
        // Add various statistics like placement percentage, 
        // average package, top recruiting companies, etc.
        
        return stats;
    }

    @Override
    public List<Object[]> getCompanyWisePlacementStats() {
        return jobService.getActiveJobCountByCompany();
    }

    @Override
    public List<Object[]> getBranchWiseePlacementStats() {
        return studentService.getStudentCountByBranch();
    }

    @Override
    public List<Student> getStudentsEligibleForJob(Long jobId) {
        Optional<Job> jobOpt = jobService.findById(jobId);
        if (jobOpt.isPresent()) {
            Job job = jobOpt.get();
            
            return studentService.findAllStudents().stream()
                .filter(student -> student.getCgpa() >= job.getMinCgpa())
                .filter(student -> student.getResumeUrl() != null) // Has resume
                .toList();
        }
        return new ArrayList<>();
    }

    @Override
    public void notifyStudentsAboutNewJob(Long jobId) {
        List<Student> eligibleStudents = getStudentsEligibleForJob(jobId);
        
        // Implementation for sending notifications
        // This could involve email service, SMS, or in-app notifications
        
        eligibleStudents.forEach(student -> {
            // Send notification logic here
            System.out.println("Notifying student: " + student.getUser().getEmail() + 
                             " about new job opportunity: " + jobId);
        });
    }

    @Override
    public void notifyShortlistedStudents(Long jobId) {
        List<Student> shortlistedStudents = getShortlistedStudents(jobId);
        
        shortlistedStudents.forEach(student -> {
            // Send shortlist notification logic here
            System.out.println("Notifying student: " + student.getUser().getEmail() + 
                             " about being shortlisted for job: " + jobId);
        });
    }
}