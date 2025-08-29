package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.service.PlacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/placement")
@CrossOrigin(origins = "*")
public class PlacementController {

    @Autowired
    private PlacementService placementService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = placementService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/dashboard/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentDashboard(@PathVariable Long studentId) {
        Map<String, Object> dashboard = placementService.getStudentDashboard(studentId);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/dashboard/company/{companyId}")
    public ResponseEntity<Map<String, Object>> getCompanyDashboard(@PathVariable Long companyId) {
        Map<String, Object> dashboard = placementService.getCompanyDashboard(companyId);
        return ResponseEntity.ok(dashboard);
    }

    @PostMapping("/job/{jobId}/process-applications")
    public ResponseEntity<String> processJobApplications(@PathVariable Long jobId) {
        try {
            placementService.processJobApplications(jobId);
            return ResponseEntity.ok("Job applications processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing applications: " + e.getMessage());
        }
    }

    @PostMapping("/job/{jobId}/generate-shortlist")
    public ResponseEntity<String> generateAndUpdateShortlists(@PathVariable Long jobId) {
        try {
            placementService.generateAndUpdateShortlists(jobId);
            return ResponseEntity.ok("Shortlist generated and updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating shortlist: " + e.getMessage());
        }
    }

    @GetMapping("/job/{jobId}/shortlisted-students")
    public ResponseEntity<List<Student>> getShortlistedStudents(@PathVariable Long jobId) {
        List<Student> students = placementService.getShortlistedStudents(jobId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/job/{jobId}/eligible-students")
    public ResponseEntity<List<Student>> getStudentsEligibleForJob(@PathVariable Long jobId) {
        List<Student> students = placementService.getStudentsEligibleForJob(jobId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/statistics/placement")
    public ResponseEntity<List<Object[]>> getPlacementStatistics() {
        List<Object[]> stats = placementService.getPlacementStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistics/company-wise")
    public ResponseEntity<List<Object[]>> getCompanyWisePlacementStats() {
        List<Object[]> stats = placementService.getCompanyWisePlacementStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistics/branch-wise")
    public ResponseEntity<List<Object[]>> getBranchWisePlacementStats() {
        List<Object[]> stats = placementService.getBranchWiseePlacementStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/job/{jobId}/notify-students")
    public ResponseEntity<String> notifyStudentsAboutNewJob(@PathVariable Long jobId) {
        try {
            placementService.notifyStudentsAboutNewJob(jobId);
            return ResponseEntity.ok("Students notified successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error notifying students: " + e.getMessage());
        }
    }

    @PostMapping("/job/{jobId}/notify-shortlisted")
    public ResponseEntity<String> notifyShortlistedStudents(@PathVariable Long jobId) {
        try {
            placementService.notifyShortlistedStudents(jobId);
            return ResponseEntity.ok("Shortlisted students notified successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error notifying shortlisted students: " + e.getMessage());
        }
    }
}