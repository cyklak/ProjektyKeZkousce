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


    private InsuranceRepository insuranceRepository;


    private UserRepository userRepository;


    private InsuredRepository insuredRepository;


    private InsuranceMapper insuranceMapper;

    public InsuranceService(InsuranceRepository insuranceRepository, UserRepository userRepository, InsuredRepository insuredRepository, InsuranceMapper insuranceMapper) {
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
        this.insuredRepository = insuredRepository;
        this.insuranceMapper = insuranceMapper;
    }


    public void create(InsuranceDTO pojisteni) {
        InsuranceEntity newPojisteni = insuranceMapper.toEntity(pojisteni);
        insuranceRepository.save(newPojisteni);
    }


    public List<InsuranceDTO> getAllByPojistenecId(Long pojistenecId) {
        List<InsuranceEntity> seznamPojisteni = insuredRepository.findById(pojistenecId).orElseThrow().getSeznamPojisteni();
        List<InsuranceDTO> insuranceDTO = new ArrayList<>();
        for (InsuranceEntity pojisteni : seznamPojisteni) {
            insuranceDTO.add(insuranceMapper.toDTO(pojisteni));
        }
        return insuranceDTO;
    }

    public List<InsuranceDTO> getPojisteniByUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<InsuredEntity> seznamPojistencu = insuredRepository.findAllByPojistnikId(userId);
        List<InsuranceEntity> result = new ArrayList<>();
        for (InsuredEntity e : seznamPojistencu) {
            result.addAll(e.getSeznamPojisteni());}
        List<InsuranceDTO> result2 = new ArrayList<>();
        for (InsuranceEntity pojisteni: result){
            result2.add(insuranceMapper.toDTO(pojisteni));}

        return result2;
    }


    public InsuranceDTO getById(long pojisteniId) {
        InsuranceEntity fetchedPojisteni = getPojisteniOrThrow(pojisteniId);

        return insuranceMapper.toDTO(fetchedPojisteni);
    }

    private InsuranceEntity getPojisteniOrThrow(long pojisteniID) {
        return insuranceRepository
                .findById(pojisteniID)
                .orElseThrow(InsuranceNotFoundException::new);
    }

    public InsuranceEntity getPojisteniEntity(long pojisteniID) {
        return insuranceRepository
                .findById(pojisteniID)
                .orElseThrow(InsuranceNotFoundException::new);
    }


    public void remove(long pojisteniId) {
        InsuranceEntity fetchedEntity = getPojisteniOrThrow(pojisteniId);
        insuranceRepository.delete(fetchedEntity);
    }


    public void edit(InsuranceDTO pojisteni) {
        InsuranceEntity fetchedPojisteni = getPojisteniOrThrow(pojisteni.getPojisteniId());
        LocalDate puvodniPlatnostod = fetchedPojisteni.getPlatnostOd();
        LocalDate puvodniPlatnostDo = fetchedPojisteni.getPlatnostDo();
        insuranceMapper.updatePojisteniEntity(pojisteni, fetchedPojisteni);
        if (puvodniPlatnostod.isBefore(LocalDate.now()) || puvodniPlatnostod.isEqual(LocalDate.now()))
            fetchedPojisteni.setPlatnostOd(puvodniPlatnostod);
        if (puvodniPlatnostDo.isBefore(LocalDate.now()))
            fetchedPojisteni.setPlatnostDo(puvodniPlatnostDo);
        insuranceRepository.save(fetchedPojisteni);
    }


    public List<InsuranceDTO> getPojisteni(int currentPage) {
        Page<InsuranceEntity> pageOfPojisteni = insuranceRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuranceEntity> pojisteniEntities = pageOfPojisteni.getContent();
        List<InsuranceDTO> result = new ArrayList<>();
        for (InsuranceEntity e : pojisteniEntities) {
            result.add(insuranceMapper.toDTO(e));
        }
        return result;
    }


}

