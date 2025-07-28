package com.xplore.interviewer.repository;

import com.xplore.interviewer.entity.AvailabilityBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailabilityRepository extends JpaRepository<AvailabilityBlock, Long> {}
