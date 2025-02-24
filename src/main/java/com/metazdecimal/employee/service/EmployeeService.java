package com.metazdecimal.employee.service;

import com.metazdecimal.employee.dto.EmployeeRequestDto;
import com.metazdecimal.employee.dto.SkillDto;
import com.metazdecimal.employee.model.Employee;
import com.metazdecimal.employee.model.Skill;
import com.metazdecimal.employee.model.Payroll;
import com.metazdecimal.employee.repository.EmployeeRepository;
import com.metazdecimal.employee.repository.SkillRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Setter
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public Employee createEmployee(EmployeeRequestDto requestDto) {
        // Check if employee with same email already exists
        // Check if employee with same email already exists
        if (employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("Record already exists with the same email: " + requestDto.getEmail());
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Map Payroll
        List<Payroll> payrolls = requestDto.getPayroll().stream().map(payrollDto -> {
            Payroll payroll = new Payroll();
            payroll.setSalary(payrollDto.getSalary());
            payroll.setSalaryMonth(payrollDto.getSalaryMonth().format(formatter)); // Convert LocalDate to String
            payroll.setPfContribution(payrollDto.getPfContribution());
            payroll.setEmployee(employee);
            return payroll;
        }).collect(Collectors.toList());

        employee.setPayroll(payrolls);

        // Save employee
        return employeeRepository.save(employee);
    }
        
    @Transactional
    public String deleteEmployee(Long employeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
        return "Employee deleted successfully";
    }

    @Transactional
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
    }

    @Transactional
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

}
