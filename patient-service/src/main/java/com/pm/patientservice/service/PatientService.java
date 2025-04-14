package com.pm.patientservice.service;

import billing.BillingResponse;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PatientService {

    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;

    public PatientService(
            BillingServiceGrpcClient billingServiceGrpcClient,
            PatientRepository patientRepository,
            KafkaProducer kafkaProducer
    ) {
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientRepository = patientRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = new ArrayList<>();
        patients.forEach(patient -> {
            patientResponseDTOs.add(PatientMapper.patientToPatientResponseDTO(patient));
        });

        return patientResponseDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = PatientMapper.patientRequestDTOToPatient(patientRequestDTO);

        if(patientRepository.existsByEmail(patient.getEmail()))
            throw new EmailAlreadyExistsException("Patient with " + patient.getEmail() + " already exists");

        Patient savedPatient = patientRepository.save(patient);

        BillingResponse billingResponse = billingServiceGrpcClient.createBillingAccount(
                savedPatient.getId().toString(),
                savedPatient.getName(),
                savedPatient.getEmail()
        );
        log.info("Created new billing account for patient {}", savedPatient.getId());

        kafkaProducer.sendEvent(savedPatient);

        return PatientMapper.patientToPatientResponseDTO(savedPatient);
    }

    public PatientResponseDTO findPatientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        return PatientMapper.patientToPatientResponseDTO(patient);
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

        return PatientMapper.patientToPatientResponseDTO(patient);
    }

    public PatientResponseDTO deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );

        patientRepository.delete(patient);
        return PatientMapper.patientToPatientResponseDTO(patient);
    }
}
