package com.xplore.interviewer.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class InterviewSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long interviewerId;
    private String interviewerName;

    @ElementCollection
    @CollectionTable(
            name = "interviewer_skills",
            joinColumns = @JoinColumn(name = "slot_id")
    )
    @Column(name = "skill")
    private Set<String> technicalSkills;

    private Integer minYearsExperience;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes; // 30, 45, 60

    private String round; // L1, L2

    @Enumerated(EnumType.STRING)
    private SlotStatus status;

    private Long bookedCandidateId; // Added this field
}