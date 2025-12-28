package com.xplore.interviewer.repository;

import com.xplore.interviewer.entity.InterviewFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<InterviewFeedback, Long> {
    
    Optional<InterviewFeedback> findByInterviewSlotId(Long interviewSlotId);
    
    List<InterviewFeedback> findByInterviewerId(Long interviewerId);
    
    List<InterviewFeedback> findByCandidateId(Long candidateId);
    
    Optional<InterviewFeedback> findByRecruitmentId(Long recruitmentId);
    
    boolean existsByInterviewSlotId(Long interviewSlotId);
}
