package com.example.placement_portal.repo;

import com.example.placement_portal.entity.Job;
import com.example.placement_portal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job, Long> {

    // Find active jobs
    List<Job> findByIsActiveTrue();

    // Find inactive jobs
    List<Job> findByIsActiveFalse();

    // Find jobs by company
    List<Job> findByCompany(Company company);
    List<Job> findByCompanyId(Long companyId);

    // Find active jobs by company
    List<Job> findByCompanyAndIsActiveTrue(Company company);

    // Find jobs with registration still open
    @Query("SELECT j FROM Job j WHERE j.registrationEnd > :currentTime AND j.isActive = true")
    List<Job> findOpenJobsForRegistration(@Param("currentTime") LocalDateTime currentTime);


    // Find jobs by minimum package range
    List<Job> findByMinPackageGreaterThanEqual(Double minPackage);

    // Find jobs by maximum package range
    List<Job> findByMaxPackageLessThanEqual(Double maxPackage);

    // Find jobs by package range
    @Query("SELECT j FROM Job j WHERE j.minPackage >= :minPkg AND j.maxPackage <= :maxPkg AND j.isActive = true")
    List<Job> findJobsByPackageRange(@Param("minPkg") Double minPackage, @Param("maxPkg") Double maxPackage);

    // Find jobs by minimum CGPA requirement
    List<Job> findByMinCgpaLessThanEqual(Double studentCgpa);

    // Find jobs by title containing
    List<Job> findByTitleContainingIgnoreCase(String title);

    // Find jobs suitable for a student (by CGPA)
    @Query("SELECT j FROM Job j WHERE j.minCgpa <= :studentCgpa AND j.isActive = true AND j.registrationEnd > :currentTime")
    List<Job> findSuitableJobsForStudent(@Param("studentCgpa") Double studentCgpa,
                                         @Param("currentTime") LocalDateTime currentTime);

    // Get jobs statistics
    @Query("SELECT j.company.name, COUNT(j) FROM Job j WHERE j.isActive = true GROUP BY j.company.name")
    List<Object[]> countActiveJobsByCompany();
}

