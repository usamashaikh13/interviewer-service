package com.xplore.interviewer.controller;

import com.xplore.interviewer.entity.AvailabilityBlock;
import com.xplore.interviewer.service.AvailabilityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public AvailabilityBlock addBlock(@RequestBody AvailabilityBlock block) {
        return availabilityService.addBlock(block);
    }

    @GetMapping
    public List<AvailabilityBlock> listBlocks() {
        return availabilityService.getAllBlocks();
    }

    @GetMapping("/{id}")
    public AvailabilityBlock getBlock(@PathVariable Long id) {
        return availabilityService.getBlockById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBlock(@PathVariable Long id) {
        availabilityService.deleteBlock(id);
    }
}
