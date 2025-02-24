package com.metazdecimal.employee.dto;

import lombok.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDto {
    private Double salary;
    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDate salaryMonth;
    private Double pfContribution;
}
