package com.example.placement_portal.repo;

import com.example.placement_portal.entity.Student;
import com.example.placement_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    // Find by roll number
    Optional<Student> findByRollNumber(String rollNumber);

    // Check if roll number exists
    boolean existsByRollNumber(String rollNumber);

    // Find by user
    Optional<Student> findByUser(User user);
    Optional<Student> findByUserId(Long userId);

    // Find by branch
    List<Student> findByBranch(String branch);

    // Find by year
    List<Student> findByYear(Integer year);

    // Find by branch and year
    List<Student> findByBranchAndYear(String branch, Integer year);

    // Find students with CGPA greater than or equal
    List<Student> findByCgpaGreaterThanEqual(Double minCgpa);

    // Find students eligible for a job (by CGPA, branch, year)
    @Query("SELECT s FROM Student s WHERE s.cgpa >= :minCgpa AND s.branch IN :branches AND s.year = :year")
    List<Student> findEligibleStudents(@Param("minCgpa") Double minCgpa,
                                       @Param("branches") List<String> branches,
                                       @Param("year") Integer year);

    // Find students with resume uploaded
    @Query("SELECT s FROM Student s WHERE s.resumeUrl IS NOT NULL")
    List<Student> findStudentsWithResume();

    // Find students without resume
    @Query("SELECT s FROM Student s WHERE s.resumeUrl IS NULL")
    List<Student> findStudentsWithoutResume();

    // Count students by branch
    @Query("SELECT s.branch, COUNT(s) FROM Student s GROUP BY s.branch")
    List<Object[]> countStudentsByBranch();
}

