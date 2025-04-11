package com.pm.patientservice.dto;

import com.pm.patientservice.dto.validators.CreatePatientValidatorGroup;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequestDTO {

    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;

    @Email
    private String email;

    @Past
    @NotNull(message = "Birthdate is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 50)
    private String address;

    @NotNull(groups = CreatePatientValidatorGroup.class, message = "Registered date is required")
    private LocalDate registeredDate;
}
