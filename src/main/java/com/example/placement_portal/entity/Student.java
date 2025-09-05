package com.example.placement_portal.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String rollNumber;

    @Column(nullable = false)
    private String branch;
    @Column(name = "\"year\"", nullable = false)
    private Integer year;
    @Column(nullable = false)
    private Double cgpa;

    private String resumeUrl;
    @ElementCollection
    private List<String> Skills;
}
