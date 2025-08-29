package com.example.placement_portal.repo;

import com.example.placement_portal.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {

    // Find by name
    Optional<Company> findByName(String name);

    // Find by HR email
    Optional<Company> findByHrEmail(String hrEmail);

    // Find by industry
    List<Company> findByIndustry(String industry);

    // Find companies by name containing (search)
    List<Company> findByNameContainingIgnoreCase(String name);

    // Find companies with active jobs
    @Query("SELECT DISTINCT c FROM Company c JOIN c.jobs j WHERE j.isActive = true")
    List<Company> findCompaniesWithActiveJobs();
}