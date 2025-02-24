package com.metazdecimal.employee.repository;

import com.metazdecimal.employee.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    
}