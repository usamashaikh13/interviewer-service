package com.xplore.interviewer.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String interviewerName;
    private String dateTime;
    private String status; // AVAILABLE, BOOKED, RESERVED, COMPLETED

    private String candidateName;
}
