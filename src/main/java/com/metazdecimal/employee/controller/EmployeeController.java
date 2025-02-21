package com.metazdecimal.employee.controller;
import com.metazdecimal.employee.dto.EmployeeRequestDto;
import com.metazdecimal.employee.dto.EmployeeResponseDto;
import com.metazdecimal.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequestDto requestDto) {
        try {
            EmployeeResponseDto response = employeeService.createEmployee(requestDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{ \"message\": \"" + e.getMessage() + "\" }");
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> deleteEmployee(@PathVariable Long employeeId) {
        try {
            EmployeeResponseDto response = employeeService.deleteEmployee(employeeId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new EmployeeResponseDto(e.getMessage()));
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long employeeId) {
        try {
            EmployeeResponseDto response = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new EmployeeResponseDto(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        List<EmployeeResponseDto> response = employeeService.getAllEmployees();
        return ResponseEntity.ok(response);
    }
}
