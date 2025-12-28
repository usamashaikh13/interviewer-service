package com.xplore.interviewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Set<String> technicalSkills;
    private int yearsExperience;
    private String designation;
    private String department;
    private String bio;
    private boolean isActive;
}
