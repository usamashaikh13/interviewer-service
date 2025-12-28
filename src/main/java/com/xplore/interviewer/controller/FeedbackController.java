package com.xplore.interviewer.controller;

import com.xplore.interviewer.dto.FeedbackRequest;
import com.xplore.interviewer.dto.FeedbackResponse;
import com.xplore.interviewer.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.submitFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/slot/{slotId}")
    public ResponseEntity<FeedbackResponse> getFeedbackBySlotId(@PathVariable Long slotId) {
        FeedbackResponse response = feedbackService.getFeedbackBySlotId(slotId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/interviewer/{interviewerId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByInterviewerId(@PathVariable Long interviewerId) {
        List<FeedbackResponse> responses = feedbackService.getFeedbackByInterviewerId(interviewerId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByCandidateId(@PathVariable Long candidateId) {
        List<FeedbackResponse> responses = feedbackService.getFeedbackByCandidateId(candidateId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/recruitment/{recruitmentId}")
    public ResponseEntity<FeedbackResponse> getFeedbackByRecruitmentId(@PathVariable Long recruitmentId) {
        FeedbackResponse response = feedbackService.getFeedbackByRecruitmentId(recruitmentId);
        return ResponseEntity.ok(response);
    }
}
