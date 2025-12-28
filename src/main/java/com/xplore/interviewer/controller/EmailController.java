package com.xplore.interviewer.controller;

import com.xplore.interviewer.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    
    private final EmailService emailService;
    
    @PostMapping("/candidate")
    public ResponseEntity<String> sendCandidateEmail(@RequestBody Map<String, Object> emailData) {
        String candidateEmail = (String) emailData.get("candidateEmail");
        String candidateName = (String) emailData.get("candidateName");
        String interviewerName = (String) emailData.get("interviewerName");
        LocalDateTime interviewTime = LocalDateTime.parse((String) emailData.get("interviewTime"));
        int durationMinutes = (Integer) emailData.get("durationMinutes");
        String round = (String) emailData.get("round");
        
        emailService.sendInterviewScheduledEmail(
                candidateEmail, candidateName, interviewerName, 
                interviewTime, durationMinutes, round);
        
        return ResponseEntity.ok("Email sent to candidate successfully");
    }
    
    @PostMapping("/interviewer")
    public ResponseEntity<String> sendInterviewerEmail(@RequestBody Map<String, Object> emailData) {
        String interviewerEmail = (String) emailData.get("interviewerEmail");
        String interviewerName = (String) emailData.get("interviewerName");
        String candidateName = (String) emailData.get("candidateName");
        String candidateEmail = (String) emailData.get("candidateEmail");
        LocalDateTime interviewTime = LocalDateTime.parse((String) emailData.get("interviewTime"));
        int durationMinutes = (Integer) emailData.get("durationMinutes");
        String round = (String) emailData.get("round");
        
        emailService.sendInterviewerNotificationEmail(
                interviewerEmail, interviewerName, candidateName, candidateEmail,
                interviewTime, durationMinutes, round);
        
        return ResponseEntity.ok("Email sent to interviewer successfully");
    }
}
