package com.xplore.interviewer.service;


import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.repository.InterviewSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    @Autowired
    private InterviewSlotRepository repository;

    public InterviewSlot addSlot(InterviewSlot slot) {
        return repository.save(slot);
    }

    public List<InterviewSlot> getAvailableSlots() {
        return repository.findByStatus("AVAILABLE");
    }

    public List<InterviewSlot> getAllSlots() {
        return repository.findAll();
    }
}
