package com.xplore.interviewer.service;

import com.xplore.interviewer.dto.RescheduleRequest;
import com.xplore.interviewer.dto.InterviewSlotResponse;
import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.entity.SlotStatus;
import com.xplore.interviewer.repository.InterviewSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
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

        ensureNoConflict(slot.getId(), slot.getInterviewerId(), slot.getStartTime(), slot.getEndTime());

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

    public InterviewSlotResponse rescheduleSlot(Long slotId, RescheduleRequest request) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        LocalDateTime endTime = request.getEndTime();
        if (endTime == null && request.getStartTime() != null && request.getDurationMinutes() != null) {
            endTime = request.getStartTime().plusMinutes(request.getDurationMinutes());
        }
        ensureNoConflict(slot.getId(), slot.getInterviewerId(), request.getStartTime(), endTime);
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(endTime);
        if (request.getDurationMinutes() != null) {
            slot.setDurationMinutes(request.getDurationMinutes());
        } else if (request.getStartTime() != null && endTime != null) {
            slot.setDurationMinutes((int) ChronoUnit.MINUTES.between(request.getStartTime(), endTime));
        }
        slot.setCancellationReason(request.getReason());
        return convertToDto(slotRepository.save(slot));
    }

    public InterviewSlotResponse cancelSlot(Long slotId, String reason) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setStatus(SlotStatus.CANCELLED);
        slot.setCancellationReason(reason);
        return convertToDto(slotRepository.save(slot));
    }

    public InterviewSlotResponse markNoShow(Long slotId) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setStatus(SlotStatus.NO_SHOW);
        return convertToDto(slotRepository.save(slot));
    }

    public InterviewSlotResponse declineAssignment(Long slotId, String reason) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        if (slot.getStatus() != SlotStatus.BOOKED && slot.getStatus() != SlotStatus.SCHEDULED) {
            throw new IllegalStateException("Only booked or scheduled slots can be declined");
        }
        slot.setStatus(SlotStatus.CANCELLED);
        slot.setDeclineReason(reason);
        return convertToDto(slotRepository.save(slot));
    }

    public String generateIcs(Long slotId) {
        InterviewSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime endTime = slot.getEndTime() != null
                ? slot.getEndTime()
                : slot.getStartTime().plusMinutes(slot.getDurationMinutes() == null ? 60 : slot.getDurationMinutes());
        return String.join("\n",
                "BEGIN:VCALENDAR",
                "VERSION:2.0",
                "PRODID:-//Xplore//Interview Platform//EN",
                "BEGIN:VEVENT",
                "UID:interview-slot-" + slot.getId() + "@xplore",
                "DTSTAMP:" + LocalDateTime.now().format(formatter),
                "DTSTART:" + slot.getStartTime().format(formatter),
                "DTEND:" + endTime.format(formatter),
                "SUMMARY:Interview - " + slot.getRound(),
                "DESCRIPTION:Interviewer: " + slot.getInterviewerName(),
                slot.getMeetingLink() == null ? "" : "LOCATION:" + slot.getMeetingLink(),
                "END:VEVENT",
                "END:VCALENDAR");
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

    private void ensureNoConflict(Long currentSlotId, Long interviewerId, LocalDateTime start, LocalDateTime end) {
        if (interviewerId == null || start == null || end == null) {
            return;
        }
        List<SlotStatus> busyStatuses = EnumSet.of(SlotStatus.AVAILABLE, SlotStatus.BOOKED, SlotStatus.SCHEDULED, SlotStatus.RESERVED)
                .stream()
                .collect(Collectors.toList());
        boolean conflict = slotRepository.findByInterviewerIdAndStatusIn(interviewerId, busyStatuses).stream()
                .filter(slot -> currentSlotId == null || !slot.getId().equals(currentSlotId))
                .filter(slot -> slot.getStartTime() != null)
                .anyMatch(slot -> overlaps(start, end, slot.getStartTime(), slot.getEndTime()));
        if (conflict) {
            throw new IllegalStateException("Interviewer already has a conflicting slot");
        }
    }

    private boolean overlaps(LocalDateTime start, LocalDateTime end, LocalDateTime existingStart, LocalDateTime existingEnd) {
        LocalDateTime normalizedExistingEnd = existingEnd == null ? existingStart.plusHours(1) : existingEnd;
        return start.isBefore(normalizedExistingEnd) && end.isAfter(existingStart);
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
        dto.setMeetingLink(slot.getMeetingLink());
        dto.setStatus(slot.getStatus());
        dto.setBookedCandidateId(slot.getBookedCandidateId());
        dto.setCancellationReason(slot.getCancellationReason());
        dto.setDeclineReason(slot.getDeclineReason());
        return dto;
    }
}
