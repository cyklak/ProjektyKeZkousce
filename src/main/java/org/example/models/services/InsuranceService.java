package org.example.models.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
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
/**
 * lombok generated constructor throws IllegalArgumentException that prevents null values in constructor arguments
 */
@AllArgsConstructor
@Service
public class InsuranceService {

    @NonNull
    private final InsuranceRepository insuranceRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final InsuredRepository insuredRepository;

    @NonNull
    private final InsuranceMapper insuranceMapper;


    /** creates a new insurance
     * @param insurance
     */
    public void create(InsuranceDTO insurance) {
        InsuranceEntity newInsurance = insuranceMapper.toEntity(insurance);
        insuranceRepository.save(newInsurance);
    }

    /**
     * @return the number of all insurances in InsuranceRepository
     */
    public Long getInsuranceCount() {
        return insuranceRepository.count();
    }


    /**
     * @param insuredId
     * @return all insurances related to an insured person
     */
    public List<InsuranceDTO> getAllByInsuredId(Long insuredId) {
        List<InsuranceEntity> insuranceList = insuredRepository.findById(insuredId).orElseThrow().getInsuranceList();
        List<InsuranceDTO> insuranceDTO = new ArrayList<>();
        for (InsuranceEntity insurance : insuranceList) {
            insuranceDTO.add(insuranceMapper.toDTO(insurance));
        }
        return insuranceDTO;
    }

    /**
     * @param userId
     * @return all insurances paid for by a policyholder
     */
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


    /**
     * @param insuranceId
     * @return a DTO of an insurance selected by its ID
     */
    public InsuranceDTO getById(long insuranceId) {
        InsuranceEntity fetchedInsurance = getInsuranceOrThrow(insuranceId);

        return insuranceMapper.toDTO(fetchedInsurance);
    }


    /**
     * @param insuranceID
     * @return an insurance entity selected by its ID
     */
    public InsuranceEntity getInsuranceOrThrow(long insuranceID) {
        return insuranceRepository
                .findById(insuranceID)
                .orElseThrow(InsuranceNotFoundException::new);
    }


    /** deletes a selected insurance from insurance repository
     * @param insuranceId
     */
    public void remove(long insuranceId) {
        InsuranceEntity fetchedEntity = getInsuranceOrThrow(insuranceId);
        insuranceRepository.delete(fetchedEntity);
    }


    /** edits a selected insurance
     * @param insurance
     */
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


    /**
     * @param currentPage
     * @return pages of 10 items of all insurances in insurance repository
     */
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

