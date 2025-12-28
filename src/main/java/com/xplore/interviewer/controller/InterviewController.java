package com.xplore.interviewer.controller;

import com.xplore.interviewer.dto.InterviewSlotResponse;
import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.entity.SlotStatus;
import com.xplore.interviewer.service.InterviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/slots")
public class InterviewController {
    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public InterviewSlot createSlot(@RequestBody InterviewSlot slot) {
        // Set default status if not provided
        if (slot.getStatus() == null) {
            slot.setStatus(SlotStatus.AVAILABLE);
        }
        return interviewService.addSlot(slot);
    }


    @GetMapping
    public List<InterviewSlotResponse> getAllSlots() {
        return interviewService.getAllSlots();
    }

    @GetMapping("/{id}")
    public InterviewSlotResponse getSlotById(@PathVariable Long id) {
        return interviewService.getSlotById(id);
    }

    @GetMapping("/available")
    public List<InterviewSlotResponse> findAvailableSlots(
            @RequestParam Set<String> skills,
            @RequestParam int minExperience,
            @RequestParam String round) {
        return interviewService.findMatchingSlots(skills, minExperience, round);
    }

    @PostMapping("/{id}/book")
    public InterviewSlotResponse bookSlot(
            @PathVariable Long id,
            @RequestParam Long candidateId) {
        return interviewService.bookSlot(id, candidateId);
    }

    @GetMapping("/available-slots")
    public List<InterviewSlotResponse> getAvailableSlots() {
        return interviewService.getAvailableSlots();
    }
}