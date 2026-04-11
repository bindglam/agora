package com.bindglam.agora.school.repository;

import com.bindglam.agora.school.PerformanceAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PerformanceAssessmentRepository extends JpaRepository<PerformanceAssessment, Long> {
    List<PerformanceAssessment> findAllByEvaluateAt(LocalDate evaluateAt);
}
