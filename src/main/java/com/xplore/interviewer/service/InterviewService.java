package com.xplore.interviewer.service;

import com.xplore.interviewer.dto.InterviewSlotResponse;
import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.entity.SlotStatus;
import com.xplore.interviewer.repository.InterviewSlotRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    private final InterviewSlotRepository slotRepository;

    public InterviewService(InterviewSlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public InterviewSlot addSlot(InterviewSlot slot) {
        if (slot.getTechnicalSkills() == null || slot.getTechnicalSkills().contains(null)) {
            throw new IllegalArgumentException("Technical skills must not be null or contain null elements");
        }

        if (slot.getDurationMinutes() == null && slot.getStartTime() != null && slot.getEndTime() != null) {
            long minutes = ChronoUnit.MINUTES.between(slot.getStartTime(), slot.getEndTime());
            slot.setDurationMinutes((int) minutes);
        }

        return slotRepository.save(slot);
    }



    public List<InterviewSlotResponse> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InterviewSlotResponse bookSlot(Long slotId, Long candidateId) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("Slot is not available");
        }

        slot.setStatus(SlotStatus.BOOKED);
        slot.setBookedCandidateId(candidateId);
        InterviewSlot savedSlot = slotRepository.save(slot);
        return convertToDto(savedSlot);
    }

    public List<InterviewSlotResponse> findMatchingSlots(Set<String> requiredSkills,
                                                         int minYears,
                                                         String round) {
        return slotRepository.findByStatusAndRoundAndMinYearsExperienceLessThanEqual(
                        SlotStatus.AVAILABLE,
                        round,
                        minYears
                ).stream()
                .filter(slot -> slot.getTechnicalSkills().containsAll(requiredSkills))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InterviewSlotResponse> getAllSlots() {
        return slotRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InterviewSlotResponse getSlotById(Long id) {
        InterviewSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        return convertToDto(slot);
    }

    private InterviewSlotResponse convertToDto(InterviewSlot slot) {
        InterviewSlotResponse dto = new InterviewSlotResponse();
        dto.setId(slot.getId());
        dto.setInterviewerId(slot.getInterviewerId());
        dto.setInterviewerName(slot.getInterviewerName());
        dto.setTechnicalSkills(slot.getTechnicalSkills());
        dto.setMinYearsExperience(slot.getMinYearsExperience());
        dto.setStartTime(slot.getStartTime());
        dto.setDurationMinutes(slot.getDurationMinutes());
        dto.setRound(slot.getRound());
        dto.setStatus(slot.getStatus());
        dto.setBookedCandidateId(slot.getBookedCandidateId());
        return dto;
    }
}