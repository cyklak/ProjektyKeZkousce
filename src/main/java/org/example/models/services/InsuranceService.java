package org.example.models.services;

import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.InsuredEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuredRepository;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.mappers.InsuranceMapper;
import org.example.models.exceptions.InsuranceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InsuranceService {


    private final InsuranceRepository insuranceRepository;


    private final UserRepository userRepository;


    private final InsuredRepository insuredRepository;


    private final InsuranceMapper insuranceMapper;

    public InsuranceService(InsuranceRepository insuranceRepository, UserRepository userRepository, InsuredRepository insuredRepository, InsuranceMapper insuranceMapper) {
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
        this.insuredRepository = insuredRepository;
        this.insuranceMapper = insuranceMapper;
    }


    public void create(InsuranceDTO insurance) {
        InsuranceEntity newInsurance = insuranceMapper.toEntity(insurance);
        insuranceRepository.save(newInsurance);
    }

    public Long getInsuranceCount() {
        return insuranceRepository.count();
    }


    public List<InsuranceDTO> getAllByInsuredId(Long insuredId) {
        List<InsuranceEntity> insuranceList = insuredRepository.findById(insuredId).orElseThrow().getInsuranceList();
        List<InsuranceDTO> insuranceDTO = new ArrayList<>();
        for (InsuranceEntity insurance : insuranceList) {
            insuranceDTO.add(insuranceMapper.toDTO(insurance));
        }
        return insuranceDTO;
    }

    public List<InsuranceDTO> getInsurancesByUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<InsuredEntity> insuredList = insuredRepository.findAllBypolicyholderId(userId);
        List<InsuranceEntity> result = new ArrayList<>();
        for (InsuredEntity e : insuredList) {
            result.addAll(e.getInsuranceList());
        }
        List<InsuranceDTO> result2 = new ArrayList<>();
        for (InsuranceEntity insurance : result) {
            result2.add(insuranceMapper.toDTO(insurance));
        }

        return result2;
    }


    public InsuranceDTO getById(long insuranceId) {
        InsuranceEntity fetchedInsurance = getInsuranceOrThrow(insuranceId);

        return insuranceMapper.toDTO(fetchedInsurance);
    }

    private InsuranceEntity getInsuranceOrThrow(long insuranceID) {
        return insuranceRepository
                .findById(insuranceID)
                .orElseThrow(InsuranceNotFoundException::new);
    }

    public InsuranceEntity getPojisteniEntity(long insuranceID) {
        return insuranceRepository
                .findById(insuranceID)
                .orElseThrow(InsuranceNotFoundException::new);
    }


    public void remove(long insuranceId) {
        InsuranceEntity fetchedEntity = getInsuranceOrThrow(insuranceId);
        insuranceRepository.delete(fetchedEntity);
    }


    public void edit(InsuranceDTO insurance) {
        InsuranceEntity fetchedInsurance = getInsuranceOrThrow(insurance.getInsuranceId());
        LocalDate originallyValidFrom = fetchedInsurance.getValidFrom();
        LocalDate originallyValidUntil = fetchedInsurance.getValidUntil();
        insuranceMapper.updateInsuranceEntity(insurance, fetchedInsurance);
        if (originallyValidFrom.isBefore(LocalDate.now()) || originallyValidFrom.isEqual(LocalDate.now()))
            fetchedInsurance.setValidFrom(originallyValidFrom);
        if (originallyValidUntil.isBefore(LocalDate.now()))
            fetchedInsurance.setValidUntil(originallyValidUntil);
        insuranceRepository.save(fetchedInsurance);
    }


    public List<InsuranceDTO> getInsurances(int currentPage) {
        Page<InsuranceEntity> pageOfInsurances = insuranceRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuranceEntity> insuranceEntities = pageOfInsurances.getContent();
        List<InsuranceDTO> result = new ArrayList<>();
        for (InsuranceEntity e : insuranceEntities) {
            result.add(insuranceMapper.toDTO(e));
        }
        return result;
    }


}

