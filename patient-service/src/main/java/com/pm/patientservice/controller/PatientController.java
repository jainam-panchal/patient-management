package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validators.CreatePatientValidatorGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/patients")
@Tag(name = "Patient", description = "APIs for Managing Patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PostMapping("/")
    @Operation(summary = "Create Patients")
    public ResponseEntity<PatientResponseDTO> createPatient(@RequestBody @Validated({Default.class, CreatePatientValidatorGroup.class}) PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Patient Details")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findPatientById(id));
    }

    @PostMapping("/{id}")
    @Operation(summary = "Update Patient Details")
    public ResponseEntity<PatientResponseDTO> updatePatientById(@PathVariable UUID id, @RequestBody @Validated({Default.class}) PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient")
    public ResponseEntity<PatientResponseDTO> deletePatientById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.deletePatient(id));
    }
}
