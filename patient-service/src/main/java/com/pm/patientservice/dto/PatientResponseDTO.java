package com.pm.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDate registerDate;
}
