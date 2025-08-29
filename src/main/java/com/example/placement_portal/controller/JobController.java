package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Job;
import com.example.placement_portal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/save")
    public ResponseEntity<Job> saveJob(@RequestBody Job job) {
        try {
            Job savedJob = jobService.saveJob(job);
            return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Job> updateJob(@RequestBody Job job) {
        try {
            Job updatedJob = jobService.updateJob(job);
            return new ResponseEntity<>(updatedJob, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> jobOpt = jobService.findById(id);
        if (jobOpt.isPresent()) {
            return new ResponseEntity<>(jobOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        try {
            List<Job> jobs = jobService.findAllJobs();
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Job>> getActiveJobs() {
        try {
            List<Job> jobs = jobService.findActiveJobs();
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Job>> getInactiveJobs() {
        try {
            List<Job> jobs = jobService.findInactiveJobs();
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Job>> getJobsByCompanyId(@PathVariable Long companyId) {
        try {
            List<Job> jobs = jobService.findByCompanyId(companyId);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/company/{companyId}/active")
    public ResponseEntity<List<Job>> getActiveJobsByCompanyId(@PathVariable Long companyId) {
        try {
            List<Job> jobs = jobService.findByCompanyId(companyId);
            List<Job> activeJobs = jobs.stream()
                    .filter(Job::getIsActive)
                    .toList();
            return new ResponseEntity<>(activeJobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/open-registration")
    public ResponseEntity<List<Job>> getOpenJobsForRegistration() {
        try {
            List<Job> jobs = jobService.findOpenJobsForRegistration();
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/package-range")
    public ResponseEntity<List<Job>> getJobsByPackageRange(
            @RequestParam Double minPackage, @RequestParam Double maxPackage) {
        try {
            List<Job> jobs = jobService.findJobsByPackageRange(minPackage, maxPackage);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/min-package/{minPackage}")
    public ResponseEntity<List<Job>> getJobsByMinPackage(@PathVariable Double minPackage) {
        try {
            List<Job> jobs = jobService.findJobsByMinPackage(minPackage);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/max-package/{maxPackage}")
    public ResponseEntity<List<Job>> getJobsByMaxPackage(@PathVariable Double maxPackage) {
        try {
            List<Job> jobs = jobService.findJobsByMaxPackage(maxPackage);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/suitable/{studentCgpa}")
    public ResponseEntity<List<Job>> getSuitableJobsForStudent(@PathVariable Double studentCgpa) {
        try {
            List<Job> jobs = jobService.findSuitableJobsForStudent(studentCgpa);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Job>> getJobsByTitle(@PathVariable String title) {
        try {
            List<Job> jobs = jobService.findJobsByTitle(title);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stats/active-count-by-company")
    public ResponseEntity<List<Object[]>> getActiveJobCountByCompany() {
        try {
            List<Object[]> stats = jobService.getActiveJobCountByCompany();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{jobId}/activate")
    public ResponseEntity<Job> activateJob(@PathVariable Long jobId) {
        try {
            Job activatedJob = jobService.activateJob(jobId);
            return new ResponseEntity<>(activatedJob, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{jobId}/deactivate")
    public ResponseEntity<Job> deactivateJob(@PathVariable Long jobId) {
        try {
            Job deactivatedJob = jobService.deactivateJob(jobId);
            return new ResponseEntity<>(deactivatedJob, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}