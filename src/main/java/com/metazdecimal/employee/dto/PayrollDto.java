package com.metazdecimal.employee.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDto {
    private Double salary;
    private LocalDate salaryMonth;
    private Double pfContribution;
}
