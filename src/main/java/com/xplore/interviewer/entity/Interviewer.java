package com.xplore.interviewer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "interviewers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interviewer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @ElementCollection
    @CollectionTable(name = "interviewer_skills", joinColumns = @JoinColumn(name = "interviewer_id"))
    @Column(name = "skill")
    private Set<String> technicalSkills;
    
    @Column(name = "years_experience", nullable = false)
    private int yearsExperience;
    
    @Column(name = "designation")
    private String designation;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "bio", length = 1000)
    private String bio;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
