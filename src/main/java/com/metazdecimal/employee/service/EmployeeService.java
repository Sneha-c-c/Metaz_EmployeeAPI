package com.metazdecimal.employee.service;
import com.metazdecimal.employee.dto.EmployeeResponseDto;
import com.metazdecimal.employee.dto.EmployeeRequestDto;
import com.metazdecimal.employee.model.Employee;
import com.metazdecimal.employee.model.Skill;
import com.metazdecimal.employee.model.Payroll;
import com.metazdecimal.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        // Check if employee with same email already exists
        //TODO: Understand how jpa implements the find by email query
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(requestDto.getEmail());
        if (existingEmployee.isPresent()) {
            throw new RuntimeException("Record already exists with same email: " + requestDto.getEmail());
        }
        // Create new Employee entity
        Employee employee = new Employee();
        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setEmail(requestDto.getEmail());
        employee.setPhoneNumber(requestDto.getPhoneNumber());
        employee.setDepartment(requestDto.getDepartment());
        employee.setDateOfJoining(requestDto.getDateOfJoining());

        // Map Skills
        List<Skill> skills = requestDto.getSkills().stream().map(skillDto -> {
            Skill skill = new Skill();
            skill.setSkillName(skillDto.getSkillName());
            skill.setExperienceYears(skillDto.getExperienceYears());
            skill.setEmployee(employee);
            return skill;
        }).collect(Collectors.toList());

        employee.setSkills(skills);

        // Map Payroll
        List<Payroll> payrolls = requestDto.getPayroll().stream().map(payrollDto -> {
            Payroll payroll = new Payroll();
            payroll.setSalary(payrollDto.getSalary());
            payroll.setSalaryMonth(payrollDto.getSalaryMonth());
            payroll.setPfContribution(payrollDto.getPfContribution());
            payroll.setEmployee(employee);
            return payroll;
        }).collect(Collectors.toList());

        employee.setPayrolls(payrolls);

        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponseDto(savedEmployee.getEmployeeId(), "Employee created successfully");
    }

    @Transactional
    public EmployeeResponseDto deleteEmployee(Long employeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee.isPresent()) {
            employeeRepository.deleteById(employeeId);
            return new EmployeeResponseDto("Employee deleted successfully");  // Only message
        } else {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
    }

    @Transactional
    public EmployeeResponseDto getEmployeeById(Long employeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }

        Employee employee = existingEmployee.get();

        // Convert Skills to DTO
        List<EmployeeResponseDto.SkillDto> skills = employee.getSkills().stream()
                .map(skill -> new EmployeeResponseDto.SkillDto(skill.getSkillName(), skill.getExperienceYears()))
                .collect(Collectors.toList());

        // Convert Payroll to DTO
        List<EmployeeResponseDto.PayrollDto> payrolls = employee.getPayrolls().stream()
                .map(payroll -> new EmployeeResponseDto.PayrollDto(
                        payroll.getSalary(),
                        payroll.getSalaryMonth(),
                        payroll.getPfContribution()))
                .collect(Collectors.toList());

        // Return the full response
        return new EmployeeResponseDto(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getDepartment(),
                employee.getDateOfJoining(),
                skills,
                payrolls
        );
    }

    @Transactional
    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(employee -> {
            // Map skills
            List<EmployeeResponseDto.SkillDto> skillDtos = employee.getSkills().stream().map(skill -> {
                EmployeeResponseDto.SkillDto skillDto = new EmployeeResponseDto.SkillDto();
                skillDto.setSkillName(skill.getSkillName());
                skillDto.setExperienceYears(skill.getExperienceYears());
                return skillDto;
            }).collect(Collectors.toList());

            // Map payroll
            List<EmployeeResponseDto.PayrollDto> payrollDtos = employee.getPayrolls().stream().map(payroll -> {
                EmployeeResponseDto.PayrollDto payrollDto = new EmployeeResponseDto.PayrollDto();
                payrollDto.setSalary(payroll.getSalary());
                payrollDto.setSalaryMonth(payroll.getSalaryMonth());
                payrollDto.setPfContribution(payroll.getPfContribution());
                return payrollDto;
            }).collect(Collectors.toList());

            return new EmployeeResponseDto(
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail(),
                    employee.getPhoneNumber(),
                    employee.getDepartment(),
                    employee.getDateOfJoining(),
                    skillDtos,
                    payrollDtos);
        }).collect(Collectors.toList());
    }

}
