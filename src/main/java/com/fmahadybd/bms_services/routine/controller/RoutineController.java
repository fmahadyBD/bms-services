package com.fmahadybd.bms_services.routine.controller;

import com.fmahadybd.bms_services.enums.DAY;
import com.fmahadybd.bms_services.routine.dto.CreateRoutineRequest;
import com.fmahadybd.bms_services.routine.dto.RoutineResponse;
import com.fmahadybd.bms_services.routine.service.RoutineService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routines")
@AllArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping
    public ResponseEntity<RoutineResponse> createRoutine(
            @Valid @RequestBody CreateRoutineRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routineService.createRoutine(req));
    }

    @PostMapping("/{routineId}/students")
    public ResponseEntity<RoutineResponse> assignStudents(
            @PathVariable Long routineId,
            @RequestBody List<Long> studentIds) {
        return ResponseEntity.ok(routineService.assignStudentsToRoutine(routineId, studentIds));
    }

    @GetMapping("/department/{department}/batch/{batch}")
    public ResponseEntity<List<RoutineResponse>> getByDepartmentAndBatch(
            @PathVariable String department,
            @PathVariable String batch) {
        return ResponseEntity.ok(routineService.getRoutineByDepartmentAndBatch(department, batch));
    }

    @GetMapping("/day/{day}")
    public ResponseEntity<List<RoutineResponse>> getByDay(@PathVariable DAY day) {
        return ResponseEntity.ok(routineService.getRoutineByDay(day));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RoutineResponse>> getForStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(routineService.getRoutineForStudent(studentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }
}