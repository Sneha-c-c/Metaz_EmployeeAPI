package com.metazdecimal.employee.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDTO {
    private Date salaryMonth;
    private double salary;
    private double pfContribution;
}