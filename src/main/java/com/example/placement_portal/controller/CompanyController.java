package com.example.placement_portal.controller;

import com.example.placement_portal.entity.Company;
import com.example.placement_portal.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> saveCompany(@RequestBody Company company) {
        try {
            Company savedCompany = companyService.saveCompany(company);
            return new ResponseEntity<>(savedCompany, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('COMPANY') and @companyServiceImpl.getCompanyRepository().findById(#company.id).get().getHrEmail() == authentication.principal.username)")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        try {
            Company updatedCompany = companyService.updateCompany(company);
            return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> companyOpt = companyService.findById(id);
        if (companyOpt.isPresent()) {
            return new ResponseEntity<>(companyOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Company> getCompanyByName(@PathVariable String name) {
        Optional<Company> companyOpt = companyService.findByName(name);
        if (companyOpt.isPresent()) {
            return new ResponseEntity<>(companyOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/hr-email/{hrEmail}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Company> getCompanyByHrEmail(@PathVariable String hrEmail) {
        Optional<Company> companyOpt = companyService.findByHrEmail(hrEmail);
        if (companyOpt.isPresent()) {
            return new ResponseEntity<>(companyOpt.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Company>> getAllCompanies() {
        try {
            List<Company> companies = companyService.findAllCompanies();
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/industry/{industry}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Company>> getCompaniesByIndustry(@PathVariable String industry) {
        try {
            List<Company> companies = companyService.findByIndustry(industry);
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Company>> searchCompaniesByName(@PathVariable String name) {
        try {
            List<Company> companies = companyService.searchCompaniesByName(name);
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active-jobs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Company>> getCompaniesWithActiveJobs() {
        try {
            List<Company> companies = companyService.findCompaniesWithActiveJobs();
            return new ResponseEntity<>(companies, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return new ResponseEntity<>("Company deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting company: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
