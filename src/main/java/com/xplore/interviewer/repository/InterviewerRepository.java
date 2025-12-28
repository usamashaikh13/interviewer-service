package com.xplore.interviewer.repository;

import com.xplore.interviewer.entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewerRepository extends JpaRepository<Interviewer, Long> {
    
    Optional<Interviewer> findByEmail(String email);
    
    List<Interviewer> findByIsActiveTrue();
    
    @Query("SELECT i FROM Interviewer i WHERE i.isActive = true " +
           "AND i.yearsExperience >= :minExperience " +
           "AND EXISTS (SELECT 1 FROM i.technicalSkills s WHERE s IN :skills)")
    List<Interviewer> findBySkillsAndExperience(
            @Param("skills") List<String> skills,
            @Param("minExperience") int minExperience
    );
}
