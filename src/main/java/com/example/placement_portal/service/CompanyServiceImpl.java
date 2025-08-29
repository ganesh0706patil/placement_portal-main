package com.example.placement_portal.service;

import com.example.placement_portal.entity.Company;
import com.example.placement_portal.repo.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    @Override
    public Company saveCompany(Company company) {
        return companyRepo.save(company);
    }

    @Override
    public Company updateCompany(Company company) {
        return companyRepo.save(company);
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepo.findById(id);
    }

    @Override
    public Optional<Company> findByName(String name) {
        return companyRepo.findByName(name);
    }

    @Override
    public Optional<Company> findByHrEmail(String hrEmail) {
        return companyRepo.findByHrEmail(hrEmail);
    }

    @Override
    public List<Company> findAllCompanies() {
        return companyRepo.findAll();
    }

    @Override
    public List<Company> findByIndustry(String industry) {
        return companyRepo.findByIndustry(industry);
    }

    @Override
    public List<Company> searchCompaniesByName(String name) {
        return companyRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Company> findCompaniesWithActiveJobs() {
        return companyRepo.findCompaniesWithActiveJobs();
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepo.deleteById(id);
    }
}
