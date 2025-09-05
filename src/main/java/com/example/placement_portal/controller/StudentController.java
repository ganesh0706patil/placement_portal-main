package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.placement_portal.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> createStudentProfile(
            @RequestBody Student studentDetails,
            // This annotation automatically gets the User object from the JWT
            @AuthenticationPrincipal User currentUser) {
        try {
            // It then passes this User object to the service layer
            Student savedStudent = studentService.createStudentProfile(studentDetails, currentUser);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateStudentProfile(
            @PathVariable Long studentId,
            @RequestBody Student updatedDetails,
            @AuthenticationPrincipal User currentUser) {
        try {
            Student savedStudent = studentService.updateStudentProfile(studentId, updatedDetails, currentUser);
            return ResponseEntity.ok(savedStudent);
        } catch (IllegalAccessException e) {
            // This catches the security exception if a user tries to edit a profile that isn't theirs.
            return ResponseEntity.status(403).body(null); // 403 Forbidden
        } catch (RuntimeException e) {
            // This catches other errors, like "student not found".
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> findById(@PathVariable Long id) {
        Optional<Student> student = studentService.findById(id);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<Student> findByRollNumber(@PathVariable String rollNumber) {
        Optional<Student> student = studentService.findByRollNumber(rollNumber);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #userId == authentication.principal.id)")
    public ResponseEntity<Student> findByUserId(@PathVariable Long userId) {
        Optional<Student> student = studentService.findByUserId(userId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findAllStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/branch/{branch}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findByBranch(@PathVariable String branch) {
        List<Student> students = studentService.findByBranch(branch);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findByYear(@PathVariable Integer year) {
        List<Student> students = studentService.findByYear(year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/branch/{branch}/year/{year}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findByBranchAndYear(
            @PathVariable String branch, 
            @PathVariable Integer year) {
        List<Student> students = studentService.findByBranchAndYear(branch, year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/cgpa/{minCgpa}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findByCgpaGreaterThanEqual(@PathVariable Double minCgpa) {
        List<Student> students = studentService.findByCgpaGreaterThanEqual(minCgpa);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/eligible")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findEligibleStudents(
            @RequestParam Double minCgpa,
            @RequestParam List<String> branches,
            @RequestParam Integer year) {
        List<Student> students = studentService.findEligibleStudents(minCgpa, branches, year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/with-resume")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findStudentsWithResume() {
        List<Student> students = studentService.findStudentsWithResume();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/without-resume")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> findStudentsWithoutResume() {
        List<Student> students = studentService.findStudentsWithoutResume();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/count-by-branch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getStudentCountByBranch() {
        List<Object[]> counts = studentService.getStudentCountByBranch();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/exists/roll/{rollNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByRollNumber(@PathVariable String rollNumber) {
        boolean exists = studentService.existsByRollNumber(rollNumber);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == authentication.principal.id)")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting student: " + e.getMessage());
        }
    }

    @PutMapping("/{studentId}/resume")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<Student> updateResume(
            @PathVariable Long studentId, 
            @RequestParam String resumeUrl) {
        try {
            Student student = studentService.updateResume(studentId, resumeUrl);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{studentId}/skills")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #studentId == authentication.principal.id)")
    public ResponseEntity<Student> updateSkills(
            @PathVariable Long studentId, 
            @RequestParam String skills) {
        try {
            Student student = studentService.updateSkills(studentId, skills);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}