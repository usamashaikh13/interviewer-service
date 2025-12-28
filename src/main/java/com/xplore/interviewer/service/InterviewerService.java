package com.xplore.interviewer.service;

import com.xplore.interviewer.dto.InterviewerDTO;
import com.xplore.interviewer.entity.Interviewer;
import com.xplore.interviewer.repository.InterviewerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewerService {
    
    private final InterviewerRepository interviewerRepository;
    
    @Transactional
    public InterviewerDTO createInterviewer(InterviewerDTO dto) {
        if (interviewerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Interviewer with email " + dto.getEmail() + " already exists");
        }
        
        Interviewer interviewer = new Interviewer();
        interviewer.setName(dto.getName());
        interviewer.setEmail(dto.getEmail());
        interviewer.setPhone(dto.getPhone());
        interviewer.setTechnicalSkills(dto.getTechnicalSkills());
        interviewer.setYearsExperience(dto.getYearsExperience());
        interviewer.setDesignation(dto.getDesignation());
        interviewer.setDepartment(dto.getDepartment());
        interviewer.setBio(dto.getBio());
        interviewer.setActive(true);
        
        Interviewer saved = interviewerRepository.save(interviewer);
        return toDTO(saved);
    }
    
    @Transactional
    public InterviewerDTO updateInterviewer(Long id, InterviewerDTO dto) {
        Interviewer interviewer = interviewerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interviewer not found with id: " + id));
        
        interviewer.setName(dto.getName());
        interviewer.setPhone(dto.getPhone());
        interviewer.setTechnicalSkills(dto.getTechnicalSkills());
        interviewer.setYearsExperience(dto.getYearsExperience());
        interviewer.setDesignation(dto.getDesignation());
        interviewer.setDepartment(dto.getDepartment());
        interviewer.setBio(dto.getBio());
        interviewer.setActive(dto.isActive());
        
        Interviewer updated = interviewerRepository.save(interviewer);
        return toDTO(updated);
    }
    
    public InterviewerDTO getInterviewerById(Long id) {
        Interviewer interviewer = interviewerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interviewer not found with id: " + id));
        return toDTO(interviewer);
    }
    
    public List<InterviewerDTO> getAllActiveInterviewers() {
        return interviewerRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<InterviewerDTO> getInterviewersBySkillsAndExperience(List<String> skills, int minExperience) {
        return interviewerRepository.findBySkillsAndExperience(skills, minExperience).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deactivateInterviewer(Long id) {
        Interviewer interviewer = interviewerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Interviewer not found with id: " + id));
        interviewer.setActive(false);
        interviewerRepository.save(interviewer);
    }
    
    private InterviewerDTO toDTO(Interviewer interviewer) {
        InterviewerDTO dto = new InterviewerDTO();
        dto.setId(interviewer.getId());
        dto.setName(interviewer.getName());
        dto.setEmail(interviewer.getEmail());
        dto.setPhone(interviewer.getPhone());
        dto.setTechnicalSkills(interviewer.getTechnicalSkills());
        dto.setYearsExperience(interviewer.getYearsExperience());
        dto.setDesignation(interviewer.getDesignation());
        dto.setDepartment(interviewer.getDepartment());
        dto.setBio(interviewer.getBio());
        dto.setActive(interviewer.isActive());
        return dto;
    }
}
