package com.xplore.interviewer.controller;

import com.xplore.interviewer.dto.InterviewerDTO;
import com.xplore.interviewer.service.InterviewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviewers")
@RequiredArgsConstructor
public class InterviewerController {
    
    private final InterviewerService interviewerService;
    
    @PostMapping
    public ResponseEntity<InterviewerDTO> createInterviewer(@RequestBody InterviewerDTO dto) {
        InterviewerDTO created = interviewerService.createInterviewer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InterviewerDTO> updateInterviewer(
            @PathVariable Long id,
            @RequestBody InterviewerDTO dto) {
        InterviewerDTO updated = interviewerService.updateInterviewer(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InterviewerDTO> getInterviewerById(@PathVariable Long id) {
        InterviewerDTO interviewer = interviewerService.getInterviewerById(id);
        return ResponseEntity.ok(interviewer);
    }
    
    @GetMapping
    public ResponseEntity<List<InterviewerDTO>> getAllActiveInterviewers() {
        List<InterviewerDTO> interviewers = interviewerService.getAllActiveInterviewers();
        return ResponseEntity.ok(interviewers);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<InterviewerDTO>> searchInterviewers(
            @RequestParam List<String> skills,
            @RequestParam int minExperience) {
        List<InterviewerDTO> interviewers = 
                interviewerService.getInterviewersBySkillsAndExperience(skills, minExperience);
        return ResponseEntity.ok(interviewers);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateInterviewer(@PathVariable Long id) {
        interviewerService.deactivateInterviewer(id);
        return ResponseEntity.noContent().build();
    }
}
