package com.example.placement_portal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_shortlists")
@Getter
@Setter
@AllArgsConstructor
public class AIShortlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private Double score;
    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false)
    private LocalDateTime generatedAt;
    public AIShortlist() {
        this.generatedAt = LocalDateTime.now();
    }
}