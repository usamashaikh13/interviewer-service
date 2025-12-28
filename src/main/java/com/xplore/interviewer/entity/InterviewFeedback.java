package com.xplore.interviewer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_feedbacks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "interview_slot_id", nullable = false)
    private Long interviewSlotId;
    
    @Column(name = "interviewer_id", nullable = false)
    private Long interviewerId;
    
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;
    
    @Column(name = "recruitment_id", nullable = false)
    private Long recruitmentId;
    
    @Column(name = "technical_rating", nullable = false)
    private int technicalRating; // 1-5
    
    @Column(name = "communication_rating", nullable = false)
    private int communicationRating; // 1-5
    
    @Column(name = "problem_solving_rating", nullable = false)
    private int problemSolvingRating; // 1-5
    
    @Column(name = "overall_rating", nullable = false)
    private int overallRating; // 1-5
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private FeedbackRecommendation recommendation;
    
    @Column(name = "strengths", length = 2000)
    private String strengths;
    
    @Column(name = "weaknesses", length = 2000)
    private String weaknesses;
    
    @Column(name = "detailed_comments", length = 5000)
    private String detailedComments;
    
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        submittedAt = LocalDateTime.now();
    }
}
