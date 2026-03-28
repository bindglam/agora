package com.bindglam.agora.school;

import com.bindglam.agora.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
}
