package com.example.placement_portal.service;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.User;
import com.example.placement_portal.repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public Student createStudentProfile(Student studentDetails, User currentUser) {
        // HERE is the check. It uses the ID from the user object we got in the controller.
        if (studentRepo.findByUserId(currentUser.getId()).isPresent()) {
            // If a profile is found, the process stops and an error is thrown.
            throw new IllegalStateException("A student profile already exists for this user.");
        }

        // This code only runs if the check passes
        studentDetails.setUser(currentUser);
        return studentRepo.save(studentDetails);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepo.save(student);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepo.findById(id);
    }

    @Override
    public Optional<Student> findByRollNumber(String rollNumber) {
        return studentRepo.findByRollNumber(rollNumber);
    }

    @Override
    public Optional<Student> findByUser(User user) {
        return studentRepo.findByUser(user);
    }

    @Override
    public Optional<Student> findByUserId(Long userId) {
        return studentRepo.findByUserId(userId);
    }

    @Override
    public List<Student> findAllStudents() {
        return studentRepo.findAll();
    }

    @Override
    public List<Student> findByBranch(String branch) {
        return studentRepo.findByBranch(branch);
    }

    @Override
    public List<Student> findByYear(Integer year) {
        return studentRepo.findByYear(year);
    }

    @Override
    public List<Student> findByBranchAndYear(String branch, Integer year) {
        return studentRepo.findByBranchAndYear(branch, year);
    }

    @Override
    public List<Student> findByCgpaGreaterThanEqual(Double minCgpa) {
        return studentRepo.findByCgpaGreaterThanEqual(minCgpa);
    }

    @Override
    public List<Student> findEligibleStudents(Double minCgpa, List<String> branches, Integer year) {
        return studentRepo.findEligibleStudents(minCgpa, branches, year);
    }

    @Override
    public List<Student> findStudentsWithResume() {
        return studentRepo.findStudentsWithResume();
    }

    @Override
    public List<Student> findStudentsWithoutResume() {
        return studentRepo.findStudentsWithoutResume();
    }

    @Override
    public List<Object[]> getStudentCountByBranch() {
        return studentRepo.countStudentsByBranch();
    }

    @Override
    public boolean existsByRollNumber(String rollNumber) {
        return studentRepo.existsByRollNumber(rollNumber);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepo.deleteById(id);
    }

    @Override
    public Student updateResume(Long studentId, String resumeUrl) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setResumeUrl(resumeUrl);
            return studentRepo.save(student);
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }

    @Override
    public Student updateStudentProfile(Long studentId, Student updatedDetails, User currentUser) throws IllegalAccessException {
        // Step 1: Fetch the existing student profile from the database using the ID from the URL.
        Student existingStudent = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student profile not found with id: " + studentId));

        // Step 2: CRITICAL SECURITY CHECK: Verify that the profile belongs to the logged-in user.
        if (!existingStudent.getUser().getId().equals(currentUser.getId())) {
            // If the IDs do not match, throw an exception to deny access.
            throw new IllegalAccessException("You are not authorized to update this profile.");
        }

        // Step 3: Apply the updates from the JSON payload to the existing record.
        // This ensures that critical fields like the user link and ID are not changed.
        existingStudent.setRollNumber(updatedDetails.getRollNumber());
        existingStudent.setBranch(updatedDetails.getBranch());
        existingStudent.setYear(updatedDetails.getYear());
        existingStudent.setCgpa(updatedDetails.getCgpa());
        existingStudent.setSkills(updatedDetails.getSkills());

        // Step 4: Save the updated profile back to the database.
        return studentRepo.save(existingStudent);
    }

    @Override
    public Student updateSkills(Long studentId, String skills) {
        Optional<Student> studentOpt = studentRepo.findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (skills != null && !skills.isEmpty()) {
                student.setSkills(Arrays.asList(skills.split(",\\s*")));
            } else {
                student.setSkills(new ArrayList<>());
            }
            return studentRepo.save(student);
        }
        throw new RuntimeException("Student not found with id: " + studentId);
    }
}