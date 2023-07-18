package org.example.models.services;

import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.InsuranceEventEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuredRepository;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.InsuranceEventRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuranceEventDTO;
import org.example.models.dto.Roles;
import org.example.models.dto.mappers.InsuranceMapper;
import org.example.models.dto.mappers.InsuranceEventMapper;
import org.example.models.exceptions.EventNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsuranceEventService {

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private InsuredRepository insuredRepository;

    @Autowired
    private InsuranceMapper insuranceMapper;

    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceEventMapper udalostMapper;

    @Autowired
    private InsuranceEventRepository udalostRepository;

    @Autowired
    private UserRepository userRepository;



    public void create(InsuranceEventDTO udalost, Long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity udalostEntity = udalostMapper.toEntity(udalost);
        udalostEntity.setPojistenecId(pojistenecId);
        udalostEntity.setPojisteni(new ArrayList<>());
        for (Long pojisteniId : udalost.getPojisteniIds()) {
            udalostEntity.getPojisteni().add(insuranceService.getPojisteniEntity(pojisteniId));
        }
        if (user.getRole().contains(Roles.POJISTNIK))
            udalostEntity.setPojistnikId(user.getUserId());
        else
            udalostEntity.setPojistnikId(user.getPojistenec().getPojistnikId());
        udalostEntity.setJmenoPojisteneho(insuredRepository.findById(pojistenecId).get().getJmeno());
        udalostEntity.setPrijmeniPojisteneho(insuredRepository.findById(pojistenecId).get().getPrijmeni());
        udalostRepository.save(udalostEntity);

    }


    public List<InsuranceEventDTO> getUdalosti(int currentPage) {
        Page<InsuranceEventEntity> pageOfPeople = udalostRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuranceEventEntity> udalostEntities = pageOfPeople.getContent();
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : udalostEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }


    public InsuranceEventDTO getById(long pojistnaUdalostId) {
        InsuranceEventEntity fetchedUdalost = getUdalostOrThrow(pojistnaUdalostId);

        return udalostMapper.toDTO(fetchedUdalost);
    }

    private InsuranceEventEntity getUdalostOrThrow(long pojistnaUdalostID) {
        return udalostRepository
                .findById(pojistnaUdalostID)
                .orElseThrow(EventNotFoundException::new);
    }


    public List<InsuranceEventDTO> getUdalostibyPojistenecId(int currentPage, Long pojistenecId) {
        Page<InsuranceEventEntity> pageOfPeople = udalostRepository.findAllBypojistenecId(PageRequest.of(currentPage, 10), pojistenecId);
        List<InsuranceEventEntity> udalostEntities = pageOfPeople.getContent();
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : udalostEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }


    public List<InsuranceEventDTO> getUdalostiByUserId(long userId) {
        List<InsuranceEventEntity> seznamUdalosti = udalostRepository.findAllBypojistnikId(userId);
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity udalost : seznamUdalosti) {
            result.add(udalostMapper.toDTO(udalost));
        }
        return result;
    }


    public void edit(InsuranceEventDTO udalostDTO, Long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity fetchedUdalost = getUdalostOrThrow(udalostDTO.getPojistnaUdalostId());
        udalostMapper.updatePojistnaUdalostEntity(udalostDTO, fetchedUdalost);
        List<InsuranceEntity> pojisteni = new ArrayList<>();
        for (Long pojisteniId : udalostDTO.getPojisteniIds()) {
            pojisteni.add(insuranceRepository.findById(pojisteniId).orElseThrow());
        }
        fetchedUdalost.setPojisteni(pojisteni);
        fetchedUdalost.setPojistenecId(pojistenecId);
        fetchedUdalost.setJmenoPojisteneho(insuredRepository.findById(pojistenecId).get().getJmeno());
        fetchedUdalost.setPrijmeniPojisteneho(insuredRepository.findById(pojistenecId).get().getPrijmeni());
        if (user.getRole().contains(Roles.POJISTNIK))
            fetchedUdalost.setPojistnikId(user.getUserId());
        else
            fetchedUdalost.setPojistnikId(user.getPojistenec().getPojistnikId());
        udalostRepository.save(fetchedUdalost);

    }


    public List<Long> filterInsurances(InsuranceEventDTO udalost) {
        List<Long> neplatnaPojisteniIds = new ArrayList<>();
        for (Long pojisteniId : udalost.getPojisteniIds()) {
            InsuranceDTO pojisteni = insuranceService.getById(pojisteniId);
            if ((pojisteni.getPlatnostOd().isAfter(udalost.getDatumUdalosti())) || (pojisteni.getPlatnostDo().isBefore(udalost.getDatumUdalosti())))
                neplatnaPojisteniIds.add(pojisteniId);
        }
        return neplatnaPojisteniIds;
    }


    public void remove(long udalostId) {
        InsuranceEventEntity fetchedEntity = getUdalostOrThrow(udalostId);
        udalostRepository.delete(fetchedEntity);
    }

}


