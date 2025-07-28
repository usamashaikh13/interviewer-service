package com.xplore.interviewer.repository;

import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    List<InterviewSlot> findByStatus(SlotStatus status);

    List<InterviewSlot> findByStatusAndRoundAndMinYearsExperienceLessThanEqual(
            SlotStatus status,
            String round,
            Integer minYearsExperience
    );
}