package com.xplore.interviewer.service;

import com.xplore.interviewer.dto.FeedbackRequest;
import com.xplore.interviewer.dto.FeedbackResponse;
import com.xplore.interviewer.entity.InterviewFeedback;
import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.entity.SlotStatus;
import com.xplore.interviewer.repository.FeedbackRepository;
import com.xplore.interviewer.repository.InterviewSlotRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    
    private static final Logger logger = LoggerFactory.getLogger(FeedbackService.class);
    
    private final FeedbackRepository feedbackRepository;
    private final InterviewSlotRepository interviewSlotRepository;
    private final RestTemplate restTemplate;
    
    @Value("${recruitment.service.url:http://localhost:8080/api/recruitments}")
    private String recruitmentServiceUrl;
    
    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        if (feedbackRepository.existsByInterviewSlotId(request.getInterviewSlotId())) {
            throw new RuntimeException("Feedback already submitted for this interview slot");
        }
        
        InterviewSlot slot = interviewSlotRepository.findById(request.getInterviewSlotId())
                .orElseThrow(() -> new RuntimeException("Interview slot not found"));
        
        if (slot.getStatus() != SlotStatus.BOOKED) {
            throw new RuntimeException("Cannot submit feedback for non-booked slot");
        }
        
        validateRating(request.getTechnicalRating(), "Technical rating");
        validateRating(request.getCommunicationRating(), "Communication rating");
        validateRating(request.getProblemSolvingRating(), "Problem solving rating");
        validateRating(request.getOverallRating(), "Overall rating");
        
        InterviewFeedback feedback = new InterviewFeedback();
        feedback.setInterviewSlotId(request.getInterviewSlotId());
        feedback.setInterviewerId(request.getInterviewerId());
        feedback.setCandidateId(request.getCandidateId());
        feedback.setRecruitmentId(request.getRecruitmentId());
        feedback.setTechnicalRating(request.getTechnicalRating());
        feedback.setCommunicationRating(request.getCommunicationRating());
        feedback.setProblemSolvingRating(request.getProblemSolvingRating());
        feedback.setOverallRating(request.getOverallRating());
        feedback.setRecommendation(request.getRecommendation());
        feedback.setStrengths(request.getStrengths());
        feedback.setWeaknesses(request.getWeaknesses());
        feedback.setDetailedComments(request.getDetailedComments());
        
        InterviewFeedback saved = feedbackRepository.save(feedback);
        
        try {
            String updateUrl = String.format("%s/%d/status?status=COMPLETED", 
                    recruitmentServiceUrl, request.getRecruitmentId());
            restTemplate.put(updateUrl, null);
            logger.info("Updated recruitment status to COMPLETED for recruitmentId: {}", 
                    request.getRecruitmentId());
        } catch (Exception e) {
            logger.error("Failed to update recruitment status: {}", e.getMessage());
        }
        
        return toResponse(saved);
    }
    
    public FeedbackResponse getFeedbackBySlotId(Long slotId) {
        InterviewFeedback feedback = feedbackRepository.findByInterviewSlotId(slotId)
                .orElseThrow(() -> new RuntimeException("Feedback not found for slot id: " + slotId));
        return toResponse(feedback);
    }
    
    public List<FeedbackResponse> getFeedbackByInterviewerId(Long interviewerId) {
        return feedbackRepository.findByInterviewerId(interviewerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public List<FeedbackResponse> getFeedbackByCandidateId(Long candidateId) {
        return feedbackRepository.findByCandidateId(candidateId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    public FeedbackResponse getFeedbackByRecruitmentId(Long recruitmentId) {
        InterviewFeedback feedback = feedbackRepository.findByRecruitmentId(recruitmentId)
                .orElseThrow(() -> new RuntimeException("Feedback not found for recruitment id: " + recruitmentId));
        return toResponse(feedback);
    }
    
    private void validateRating(int rating, String fieldName) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(fieldName + " must be between 1 and 5");
        }
    }
    
    private FeedbackResponse toResponse(InterviewFeedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setId(feedback.getId());
        response.setInterviewSlotId(feedback.getInterviewSlotId());
        response.setInterviewerId(feedback.getInterviewerId());
        response.setCandidateId(feedback.getCandidateId());
        response.setRecruitmentId(feedback.getRecruitmentId());
        response.setTechnicalRating(feedback.getTechnicalRating());
        response.setCommunicationRating(feedback.getCommunicationRating());
        response.setProblemSolvingRating(feedback.getProblemSolvingRating());
        response.setOverallRating(feedback.getOverallRating());
        response.setRecommendation(feedback.getRecommendation());
        response.setStrengths(feedback.getStrengths());
        response.setWeaknesses(feedback.getWeaknesses());
        response.setDetailedComments(feedback.getDetailedComments());
        response.setSubmittedAt(feedback.getSubmittedAt());
        return response;
    }
}
