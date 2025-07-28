package com.xplore.interviewer.dto;

import com.xplore.interviewer.entity.SlotStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class InterviewSlotResponse {
    private Long id;
    private Long interviewerId;
    private String interviewerName;
    private Set<String> technicalSkills;
    private Integer minYearsExperience;
    private LocalDateTime startTime;
    private Integer durationMinutes;
    private String round;
    private SlotStatus status;
    private Long bookedCandidateId;
}