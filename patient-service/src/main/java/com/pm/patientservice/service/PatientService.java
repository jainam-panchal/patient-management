package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = new ArrayList<>();
        patients.forEach(patient -> {
            patientResponseDTOs.add(patientMapper.patientToPatientResponseDTO(patient));
        });

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = patientMapper.patientRequestDTOToPatient(patientRequestDTO);

        if(patientRepository.existsByEmail(patient.getEmail()))
            throw new EmailAlreadyExistsException("Patient with " + patient.getEmail() + " already exists");

        patientRepository.save(patient);
        return patientMapper.patientToPatientResponseDTO(patient);
    }

    public PatientResponseDTO findPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        return patientMapper.patientToPatientResponseDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id))
            throw new EmailAlreadyExistsException("Patient with " + patient.getEmail() + " already exists");

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(patientRequestDTO.getDateOfBirth());
        patient.setAddress(patient.getAddress());

        patientRepository.save(patient);

        return patientMapper.patientToPatientResponseDTO(patient);
    }

    public PatientResponseDTO deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        patientRepository.delete(patient);
        return patientMapper.patientToPatientResponseDTO(patient);
    }
}
