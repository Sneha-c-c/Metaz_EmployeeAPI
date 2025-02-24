package com.metazdecimal.employee.service;

import com.metazdecimal.employee.dto.EmployeeRequestDto;
import com.metazdecimal.employee.dto.PayrollDto;
import com.metazdecimal.employee.dto.SkillDto;
import com.metazdecimal.employee.model.Employee;
import com.metazdecimal.employee.model.Skill;
import com.metazdecimal.employee.model.Payroll;
import com.metazdecimal.employee.repository.EmployeeRepository;
import com.metazdecimal.employee.repository.SkillRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
        

        // Map Payroll
        List<Payroll> payrolls = requestDto.getPayroll().stream().map(payrollDto -> {
            Payroll payroll = new Payroll();
            payroll.setSalary(payrollDto.getSalary());
            payroll.setSalaryMonth(payrollDto.getSalaryMonth()); // Convert LocalDate to String
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

    @Transactional
    public String updateEmployeeSkills(Long employeeId, List<SkillDto> skillDtos) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            return "Employee not found";
        }

        Employee employee = optionalEmployee.get();
        List<Skill> existingSkills = employee.getSkills();

        for (SkillDto skillDto : skillDtos) {
            Optional<Skill> existingSkill = existingSkills.stream()
                    .filter(skill -> skill.getSkillName().equalsIgnoreCase(skillDto.getSkillName()))
                    .findFirst();

            if (existingSkill.isPresent()) {
                // Update existing skill
                Skill skillToUpdate = existingSkill.get();
                skillToUpdate.setExperienceYears(skillDto.getExperienceYears());
                skillRepository.save(skillToUpdate);
            } else {
                // Add new skill if it doesn't exist
                Skill newSkill = new Skill();
                newSkill.setSkillName(skillDto.getSkillName());
                newSkill.setExperienceYears(skillDto.getExperienceYears());
                newSkill.setEmployee(employee);
                skillRepository.save(newSkill);
            }
        }

        return "Employee skills updated successfully";
    }  

    
    @Transactional
    public String updateEmployeePayroll(Long employeeId, List<PayrollDto> payrollDtos) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            return "Employee not found";
        }

        Employee employee = optionalEmployee.get();
        List<Payroll> existingPayrolls = employee.getPayroll();

        for (PayrollDto payrollDto : payrollDtos) {
            Optional<Payroll> existingPayroll = existingPayrolls.stream()
                    .filter(payroll -> payroll.getSalaryMonth().equals(payrollDto.getSalaryMonth())) // Compare
                                                                                                     // LocalDate
                                                                                                     // directly
                    .findFirst();

            if (existingPayroll.isPresent()) {
                // Update existing payroll details
                Payroll payrollToUpdate = existingPayroll.get();
                payrollToUpdate.setSalary(payrollDto.getSalary());
                payrollToUpdate.setPfContribution(payrollDto.getPfContribution());
            } else {
                // Add new payroll entry
                Payroll newPayroll = new Payroll();
                newPayroll.setSalary(payrollDto.getSalary());
                newPayroll.setSalaryMonth(payrollDto.getSalaryMonth()); // Ensure LocalDate
                newPayroll.setPfContribution(payrollDto.getPfContribution());
                newPayroll.setEmployee(employee);
                existingPayrolls.add(newPayroll);
            }
        }

        employeeRepository.save(employee); // Save changes

        return "Employee payroll updated successfully";
    }
    
    @Transactional
    public Map<String, Object> getEmployeePayroll(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found");
        }

        Employee employee = optionalEmployee.get();
        List<PayrollDto> payrollDtos = employee.getPayroll().stream()
                .map(payroll -> {
                    PayrollDto dto = new PayrollDto();
                    dto.setSalary(payroll.getSalary());
                    dto.setSalaryMonth(payroll.getSalaryMonth()); // Ensure salaryMonth is LocalDate
                    dto.setPfContribution(payroll.getPfContribution());
                    return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("employeeId", employee.getEmployeeId());
        response.put("payroll", payrollDtos);

        return response;
    }
    


}