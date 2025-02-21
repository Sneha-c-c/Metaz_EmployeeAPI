package com.metazdecimal.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private LocalDate dateOfJoining;
    private List<SkillDto> skills;
    private List<PayrollDto> payroll;

    private String message; // ✅ Only for Create, Update, and Delete responses

    // ✅ Constructor for Create API Response
    public EmployeeResponseDto(Long employeeId, String message) {
        this.employeeId = employeeId;
        this.message = message;
    }

    // ✅ Constructor for Delete API Response
    public EmployeeResponseDto(String message) {
        this.message = message;
    }

    // ✅ Constructor for Get All Employees & Get Employee by ID
    public EmployeeResponseDto(Long employeeId, String firstName, String lastName, String email,
            String phoneNumber, String department, LocalDate dateOfJoining,
            List<SkillDto> skills, List<PayrollDto> payroll) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.dateOfJoining = dateOfJoining;
        this.skills = skills;
        this.payroll = payroll;
    }

    // ✅ Constructor for Get Employee Payroll API (Only Payroll Details)
    public static EmployeeResponseDto payrollResponse(Long employeeId, List<PayrollDto> payroll) {
        EmployeeResponseDto response = new EmployeeResponseDto();
        response.setEmployeeId(employeeId);
        response.setPayroll(payroll);
        return response;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillDto {
        private String skillName;
        private int experienceYears;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PayrollDto {
        private double salary;
        private LocalDate salaryMonth;
        private double pfContribution;
    }
}
