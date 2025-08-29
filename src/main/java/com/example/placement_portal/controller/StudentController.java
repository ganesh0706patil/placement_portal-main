package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudent(student);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<Student> findByUserId(@PathVariable Long userId) {
        Optional<Student> student = studentService.findByUserId(userId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> findAllStudents() {
        List<Student> students = studentService.findAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/branch/{branch}")
    public ResponseEntity<List<Student>> findByBranch(@PathVariable String branch) {
        List<Student> students = studentService.findByBranch(branch);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Student>> findByYear(@PathVariable Integer year) {
        List<Student> students = studentService.findByYear(year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/branch/{branch}/year/{year}")
    public ResponseEntity<List<Student>> findByBranchAndYear(
            @PathVariable String branch, 
            @PathVariable Integer year) {
        List<Student> students = studentService.findByBranchAndYear(branch, year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/cgpa/{minCgpa}")
    public ResponseEntity<List<Student>> findByCgpaGreaterThanEqual(@PathVariable Double minCgpa) {
        List<Student> students = studentService.findByCgpaGreaterThanEqual(minCgpa);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/eligible")
    public ResponseEntity<List<Student>> findEligibleStudents(
            @RequestParam Double minCgpa,
            @RequestParam List<String> branches,
            @RequestParam Integer year) {
        List<Student> students = studentService.findEligibleStudents(minCgpa, branches, year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/with-resume")
    public ResponseEntity<List<Student>> findStudentsWithResume() {
        List<Student> students = studentService.findStudentsWithResume();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/without-resume")
    public ResponseEntity<List<Student>> findStudentsWithoutResume() {
        List<Student> students = studentService.findStudentsWithoutResume();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/count-by-branch")
    public ResponseEntity<List<Object[]>> getStudentCountByBranch() {
        List<Object[]> counts = studentService.getStudentCountByBranch();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/exists/roll/{rollNumber}")
    public ResponseEntity<Boolean> existsByRollNumber(@PathVariable String rollNumber) {
        boolean exists = studentService.existsByRollNumber(rollNumber);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting student: " + e.getMessage());
        }
    }

    @PutMapping("/{studentId}/resume")
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