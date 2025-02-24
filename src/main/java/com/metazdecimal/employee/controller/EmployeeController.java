package com.metazdecimal.employee.controller;

import com.metazdecimal.employee.dto.EmployeeRequestDto;
import com.metazdecimal.employee.model.Employee;
import com.metazdecimal.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequestDto requestDto) {
        try {
            Employee savedEmployee = employeeService.createEmployee(requestDto);

            // Create response in required format
            Map<String, Object> response = new HashMap<>();
            response.put("employeeId", savedEmployee.getEmployeeId());
            response.put("message", "Employee created successfully");

            return ResponseEntity.ok().body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        try {
            String message = employeeService.deleteEmployee(employeeId);

            // Return JSON response
            return ResponseEntity.ok().body(Map.of("message", message));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long employeeId) {
        try {
            Employee employee = employeeService.getEmployeeById(employeeId);
            return ResponseEntity.ok().body(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{employeeId}/skills")
    public ResponseEntity<?> updateEmployeeSkills(@PathVariable Long employeeId,
            @RequestBody EmployeeRequestDto requestDto) {
        String message = employeeService.updateEmployeeSkills(employeeId, requestDto.getSkills());

        if (message.equals("Employee not found")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Employee not found\"}");
        }

        return ResponseEntity.ok("{\"message\": \"" + message + "\"}");
    }

    @PutMapping("/{employeeId}/payroll")
    public ResponseEntity<?> updateEmployeePayroll(@PathVariable Long employeeId,
            @RequestBody EmployeeRequestDto requestDto) {
        String message = employeeService.updateEmployeePayroll(employeeId, requestDto.getPayroll());

        if (message.equals("Employee not found")) {
            return ResponseEntity.badRequest().body("{\"message\": \"Employee not found\"}");
        }

        return ResponseEntity.ok("{\"message\": \"" + message + "\"}");
    }

    @GetMapping("/{employeeId}/payroll")
    public ResponseEntity<?> getEmployeePayroll(@PathVariable Long employeeId) {
        try {
            Map<String, Object> payrollData = employeeService.getEmployeePayroll(employeeId);
            return ResponseEntity.ok(payrollData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
