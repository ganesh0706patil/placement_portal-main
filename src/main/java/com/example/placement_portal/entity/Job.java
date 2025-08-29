package com.example.placement_portal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double minPackage;
    @Column(nullable = false)
    private Double maxPackage;
    @Column(nullable = false)
    private Double minCgpa;

    @ElementCollection
    private List<String> requiredSkills;
    
    @Column(nullable = false)
    private LocalDateTime registrationEnd;
    @Column(nullable = false)
    private Boolean isActive;
    public Job(){
        this.isActive = true;
    }
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AIShortlist> aiShortlists;
}

