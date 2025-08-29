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