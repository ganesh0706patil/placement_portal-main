package com.example.placement_portal.service;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.User;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student saveStudent(Student student);
    Student createStudentProfile(Student studentDetails, User currentUser);
    Student updateStudentProfile(Long studentId, Student updatedDetails, User currentUser) throws IllegalAccessException;
    Student updateStudent(Student student);
    Optional<Student> findById(Long id);
    Optional<Student> findByRollNumber(String rollNumber);
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);
    List<Student> findAllStudents();
    List<Student> findByBranch(String branch);
    List<Student> findByYear(Integer year);
    List<Student> findByBranchAndYear(String branch, Integer year);
    List<Student> findByCgpaGreaterThanEqual(Double minCgpa);
    List<Student> findEligibleStudents(Double minCgpa, List<String> branches, Integer year);
    List<Student> findStudentsWithResume();
    List<Student> findStudentsWithoutResume();
    List<Object[]> getStudentCountByBranch();
    boolean existsByRollNumber(String rollNumber);
    void deleteStudent(Long id);
    Student updateResume(Long studentId, String resumeUrl);
    Student updateSkills(Long studentId, String skills);}