package com.example.placement_portal.controller;

import com.example.placement_portal.entity.AIShortlist;
import com.example.placement_portal.service.AIShortlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ai-shortlist")
@CrossOrigin(origins = "*")
public class AIShortlistController {

    @Autowired
    private AIShortlistService aiShortlistService;

    @PostMapping("/save")
    public ResponseEntity<AIShortlist> saveAIShortlist(@RequestBody AIShortlist aiShortlist) {
        try {
            AIShortlist savedShortlist = aiShortlistService.saveAIShortlist(aiShortlist);
            return new ResponseEntity<>(savedShortlist, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<AIShortlist> updateAIShortlist(@RequestBody AIShortlist aiShortlist) {
        try {
            AIShortlist updatedShortlist = aiShortlistService.updateAIShortlist(aiShortlist);
            return new ResponseEntity<>(updatedShortlist, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIShortlist> getAIShortlistById(@PathVariable Long id) {
        Optional<AIShortlist> shortlistOpt = aiShortlistService.findById(id);
        if (shortlistOpt.isPresent()) {
            return new ResponseEntity<>(shortlistOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AIShortlist>> getAllAIShortlists() {
        try {
            List<AIShortlist> shortlists = aiShortlistService.findAllAIShortlists();
            return new ResponseEntity<>(shortlists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<AIShortlist>> getAIShortlistByJobId(@PathVariable Long jobId) {
        try {
            List<AIShortlist> shortlists = aiShortlistService.findByJobId(jobId);
            return new ResponseEntity<>(shortlists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AIShortlist>> getAIShortlistByStudentId(@PathVariable Long studentId) {
        try {
            List<AIShortlist> shortlists = aiShortlistService.findByStudentId(studentId);
            return new ResponseEntity<>(shortlists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/{jobId}/ranked")
    public ResponseEntity<List<AIShortlist>> getAIShortlistByJobRanked(@PathVariable Long jobId) {
        try {
            List<AIShortlist> shortlists = aiShortlistService.findByJobId(jobId);
            return new ResponseEntity<>(shortlists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/{jobId}/top/{limit}")
    public ResponseEntity<List<AIShortlist>> getTopRankedByJob(@PathVariable Long jobId, @PathVariable int limit) {
        try {
            List<AIShortlist> shortlists = aiShortlistService.findByJobId(jobId);
            List<AIShortlist> topShortlists = shortlists.stream()
                    .sorted((a, b) -> Integer.compare(a.getRank(), b.getRank()))
                    .limit(limit)
                    .toList();
            return new ResponseEntity<>(topShortlists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generate/{jobId}")
    public ResponseEntity<String> generateAIShortlist(@PathVariable Long jobId) {
        try {
            aiShortlistService.generateAIShortlist(jobId);
            return new ResponseEntity<>("AI Shortlist generated successfully for job ID: " + jobId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error generating AI shortlist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAIShortlist(@PathVariable Long id) {
        try {
            aiShortlistService.deleteAIShortlist(id);
            return new ResponseEntity<>("AI Shortlist deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting AI shortlist: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<String> deleteAIShortlistByJobId(@PathVariable Long jobId) {
        try {
            aiShortlistService.deleteByJobId(jobId);
            return new ResponseEntity<>("AI Shortlists deleted successfully for job ID: " + jobId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting AI shortlists: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}