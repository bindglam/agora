package com.bindglam.agora.school.controller;

import com.bindglam.agora.school.MealMenu;
import com.bindglam.agora.school.PerformanceAssessmentDTO;
import com.bindglam.agora.school.SchoolRequests;
import com.bindglam.agora.school.Timetable;
import com.bindglam.agora.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/school-api")
@RequiredArgsConstructor
public final class SchoolController {
    private final SchoolService schoolService;

    @GetMapping(value = "/timetable")
    public ResponseEntity<Timetable> getTimetable(@DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return ResponseEntity.ok(schoolService.getTimetable(date).join());
    }

    @GetMapping(value = "/mealmenu")
    public ResponseEntity<MealMenu> getMealMenu(@DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return ResponseEntity.ok(schoolService.getMealMenu(date).join());
    }

    @GetMapping(value = "/performance-assessments")
    public ResponseEntity<List<PerformanceAssessmentDTO>> getPerformanceAssessments(@DateTimeFormat(pattern = "yyyyMMdd") LocalDate date) {
        return ResponseEntity.ok(schoolService.getPerformanceAssessments(date));
    }

    @PostMapping(value = "/performance-assessment")
    public ResponseEntity<SchoolRequests.PerformanceAssessment.Create.Response> createPerformanceAssessment(@RequestBody SchoolRequests.PerformanceAssessment.Create request) {
        return ResponseEntity.ok(schoolService.createPerformanceAssessment(request));
    }
}
