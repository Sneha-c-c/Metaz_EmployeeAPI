package com.metazdecimal.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;
    private String department;

    @NotNull(message = "Date of joining is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfJoining;

    private List<SkillDto> skills;
    private List<PayrollDto> payroll;

    @Getter
    @Setter
    public static class SkillDto {
        @NotBlank(message = "Skill name is required")
        private String skillName;

        @NotNull(message = "Experience years is required")
        private Integer experienceYears;
    }

    @Getter
    @Setter
    public static class PayrollDto {
        @NotNull(message = "Salary is required")
        private Double salary;

        @NotNull(message = "Salary month is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate salaryMonth;

        @NotNull(message = "PF contribution is required")
        private Double pfContribution;
    }

    // DTO for updating payroll
    @Getter
    @Setter
    public static class EmployeePayrollUpdateRequestDto {
        @NotNull(message = "Payroll list cannot be empty")
        private List<PayrollDto> payroll;
    }

}
