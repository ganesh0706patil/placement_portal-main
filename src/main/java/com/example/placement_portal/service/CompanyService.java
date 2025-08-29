package com.example.placement_portal.service;

import com.example.placement_portal.entity.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company saveCompany(Company company);
    Company updateCompany(Company company);
    Optional<Company> findById(Long id);
    Optional<Company> findByName(String name);
    Optional<Company> findByHrEmail(String hrEmail);
    List<Company> findAllCompanies();
    List<Company> findByIndustry(String industry);
    List<Company> searchCompaniesByName(String name);
    List<Company> findCompaniesWithActiveJobs();
    void deleteCompany(Long id);
}