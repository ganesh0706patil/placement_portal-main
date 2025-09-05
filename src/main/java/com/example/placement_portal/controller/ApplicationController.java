package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Application;
import com.example.placement_portal.entity.ApplicationStatus;
import com.example.placement_portal.service.ApplicationService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/save")
    public ResponseEntity<Application> saveApplication(@RequestBody Application application) {
        try {
            Application savedApplication = applicationService.saveApplication(application);
            return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Application> updateApplication(@RequestBody Application application) {
        try {
            Application updatedApplication = applicationService.updateApplication(application);
            return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Optional<Application> applicationOpt = applicationService.findById(id);
        if (applicationOpt.isPresent()) {
            return new ResponseEntity<>(applicationOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Application>> getAllApplications() {
        try {
            List<Application> applications = applicationService.findAllApplications();
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<List<Application>> getApplicationsByStudentId(@PathVariable Long studentId) {
        try {
            List<Application> applications = applicationService.findByStudentId(studentId);
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    public ResponseEntity<List<Application>> getApplicationsByJobId(@PathVariable Long jobId) {
        try {
            List<Application> applications = applicationService.findByJobId(jobId);
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Application>> getApplicationsByStatus(@PathVariable ApplicationStatus status) {
        try {
            List<Application> applications = applicationService.findByStatus(status);
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Application>> getApplicationsByStudentAndStatus(
            @PathVariable Long studentId, @PathVariable ApplicationStatus status) {
        try {
            // This would need Student entity, but we can work around it
            List<Application> applications = applicationService.findByStudentId(studentId);
            List<Application> filteredApplications = applications.stream()
                    .filter(app -> app.getStatus() == status)
                    .toList();
            return new ResponseEntity<>(filteredApplications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<List<Application>> getApplicationsByJobAndStatus(
            @PathVariable Long jobId, @PathVariable ApplicationStatus status) {
        try {
            List<Application> applications = applicationService.findByJobId(jobId);
            List<Application> filteredApplications = applications.stream()
                    .filter(app -> app.getStatus() == status)
                    .toList();
            return new ResponseEntity<>(filteredApplications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Application>> getRecentApplications() {
        try {
            List<Application> applications = applicationService.findRecentApplications();
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Application>> getApplicationsByCompanyId(@PathVariable Long companyId) {
        try {
            List<Application> applications = applicationService.findApplicationsByCompanyId(companyId);
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/selected")
    public ResponseEntity<List<Application>> getSelectedApplications() {
        try {
            List<Application> applications = applicationService.findSelectedApplications();
            return new ResponseEntity<>(applications, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/count/student/{studentId}")
    public ResponseEntity<Long> getApplicationCountByStudent(@PathVariable Long studentId) {
        try {
            // This would need Student entity, let's work around it
            List<Application> applications = applicationService.findByStudentId(studentId);
            return new ResponseEntity<>((long) applications.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{applicationId}/status/{status}")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long applicationId, @PathVariable ApplicationStatus status) {
        try {
            Application updatedApplication = applicationService.updateApplicationStatus(applicationId, status);
            return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/apply/{studentId}/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Application> applyForJob(@PathVariable Long studentId, @PathVariable Long jobId) {
        try {
            Application application = applicationService.applyForJob(studentId, jobId);
            return new ResponseEntity<>(application, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return new ResponseEntity<>("Application deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}