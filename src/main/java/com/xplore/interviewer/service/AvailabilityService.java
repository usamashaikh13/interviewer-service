package com.xplore.interviewer.service;

import com.xplore.interviewer.entity.AvailabilityBlock;
import com.xplore.interviewer.repository.AvailabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvailabilityService {
    private final AvailabilityRepository repo;

    public AvailabilityService(AvailabilityRepository repo) {
        this.repo = repo;
    }

    public AvailabilityBlock addBlock(AvailabilityBlock block) {
        return repo.save(block);
    }

    public List<AvailabilityBlock> getAllBlocks() {
        return repo.findAll();
    }

    public AvailabilityBlock getBlockById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Block not found"));
    }

    public void deleteBlock(Long id) {
        repo.deleteById(id);
    }
}
