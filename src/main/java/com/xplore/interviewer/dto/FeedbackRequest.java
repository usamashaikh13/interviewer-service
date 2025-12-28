package com.xplore.interviewer.dto;

import com.xplore.interviewer.entity.FeedbackRecommendation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {
    private Long interviewSlotId;
    private Long interviewerId;
    private Long candidateId;
    private Long recruitmentId;
    private int technicalRating;
    private int communicationRating;
    private int problemSolvingRating;
    private int overallRating;
    private FeedbackRecommendation recommendation;
    private String strengths;
    private String weaknesses;
    private String detailedComments;
}
