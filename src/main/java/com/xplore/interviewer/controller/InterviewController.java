package com.xplore.interviewer.controller;

import com.xplore.interviewer.entity.InterviewSlot;
import com.xplore.interviewer.repository.InterviewSlotRepository;
import com.xplore.interviewer.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public InterviewSlot createSlot(@RequestBody InterviewSlot slot) {
        System.out.println("Received POST request: " + slot);
        return interviewService.addSlot(slot);
    }

    @GetMapping
    public List<InterviewSlot> getAll() {
        System.out.println("Received GET request");
        return interviewService.getAllSlots();
    }

    @GetMapping("/test")
    public String testApi() {
        System.out.println("🔥 /test endpoint hit");
        return "Controller is working!";
    }
}

