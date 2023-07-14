package org.example.models.services;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojistenecRepository;
import org.example.data.repositories.PojisteniRepository;
import org.example.data.repositories.PojistnaUdalostRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;
import org.example.models.dto.Role;
import org.example.models.dto.mappers.PojisteniMapper;
import org.example.models.dto.mappers.PojistnaUdalostMapper;
import org.example.models.exceptions.PojisteniNotFoundException;
import org.example.models.exceptions.UdalostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PojistnaUdalostServiceImpl implements PojistnaUdalostService {

    @Autowired
    private PojisteniRepository pojisteniRepository;

    @Autowired
    private PojistenecRepository pojistenecRepository;

    @Autowired
    private PojisteniMapper pojisteniMapper;

    @Autowired
    private PojisteniService pojisteniService;
    @Autowired
    private PojistnaUdalostMapper udalostMapper;

    @Autowired
    private PojistnaUdalostRepository udalostRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void create(PojistnaUdalostDTO udalost, Long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PojistnaUdalostEntity udalostEntity = udalostMapper.toEntity(udalost);
        udalostEntity.setPojistenecId(pojistenecId);
        udalostEntity.setPojisteni(new ArrayList<>());
        for (Long pojisteniId : udalost.getPojisteniIds()) {
            udalostEntity.getPojisteni().add(pojisteniService.getPojisteniEntity(pojisteniId));
        }
        if (user.getRole().contains(Role.POJISTNIK))
            udalostEntity.setPojistnikId(user.getUserId());
        else
            udalostEntity.setPojistnikId(user.getPojistenec().getPojistnikId());
        udalostEntity.setJmenoPojisteneho(pojistenecRepository.findById(pojistenecId).get().getJmeno());
        udalostEntity.setPrijmeniPojisteneho(pojistenecRepository.findById(pojistenecId).get().getPrijmeni());
        udalostRepository.save(udalostEntity);

    }

    @Override
    public List<PojistnaUdalostDTO> getUdalosti(int currentPage) {
        Page<PojistnaUdalostEntity> pageOfPeople = udalostRepository.findAll(PageRequest.of(currentPage, 10));
        List<PojistnaUdalostEntity> udalostEntities = pageOfPeople.getContent();
        List<PojistnaUdalostDTO> result = new ArrayList<>();
        for (PojistnaUdalostEntity e : udalostEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }

    @Override
    public PojistnaUdalostDTO getById(long pojistnaUdalostId) {
        PojistnaUdalostEntity fetchedUdalost = getUdalostOrThrow(pojistnaUdalostId);

        return udalostMapper.toDTO(fetchedUdalost);
    }

    private PojistnaUdalostEntity getUdalostOrThrow(long pojistnaUdalostID) {
        return udalostRepository
                .findById(pojistnaUdalostID)
                .orElseThrow(UdalostNotFoundException::new);
    }

    @Override
    public List<PojistnaUdalostDTO> getUdalostibyPojistenecId(int currentPage, Long pojistenecId) {
        Page<PojistnaUdalostEntity> pageOfPeople = udalostRepository.findAllBypojistenecId(PageRequest.of(currentPage, 10), pojistenecId);
        List<PojistnaUdalostEntity> udalostEntities = pageOfPeople.getContent();
        List<PojistnaUdalostDTO> result = new ArrayList<>();
        for (PojistnaUdalostEntity e : udalostEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }

    @Override
    public List<PojistnaUdalostDTO> getUdalostiByUserId(long userId) {
        List<PojistnaUdalostEntity> seznamUdalosti = udalostRepository.findAllBypojistnikId(userId);
        List<PojistnaUdalostDTO> result = new ArrayList<>();
        for (PojistnaUdalostEntity udalost : seznamUdalosti) {
            result.add(udalostMapper.toDTO(udalost));
        }
        return result;
    }

    @Override
    public void edit(PojistnaUdalostDTO udalostDTO, Long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PojistnaUdalostEntity fetchedUdalost = getUdalostOrThrow(udalostDTO.getPojistnaUdalostId());
        udalostMapper.updatePojistnaUdalostEntity(udalostDTO, fetchedUdalost);
        List<PojisteniEntity> pojisteni = new ArrayList<>();
        for (Long pojisteniId : udalostDTO.getPojisteniIds()) {
            pojisteni.add(pojisteniRepository.findById(pojisteniId).orElseThrow());
        }
        fetchedUdalost.setPojisteni(pojisteni);
        fetchedUdalost.setPojistenecId(pojistenecId);
        fetchedUdalost.setJmenoPojisteneho(pojistenecRepository.findById(pojistenecId).get().getJmeno());
        fetchedUdalost.setPrijmeniPojisteneho(pojistenecRepository.findById(pojistenecId).get().getPrijmeni());
        if (user.getRole().contains(Role.POJISTNIK))
            fetchedUdalost.setPojistnikId(user.getUserId());
        else
            fetchedUdalost.setPojistnikId(user.getPojistenec().getPojistnikId());
        udalostRepository.save(fetchedUdalost);

    }

    @Override
    public List<Long> filtrujPojisteni(PojistnaUdalostDTO udalost) {
        List<Long> neplatnaPojisteniIds = new ArrayList<>();
        for (Long pojisteniId : udalost.getPojisteniIds()) {
            PojisteniDTO pojisteni = pojisteniService.getById(pojisteniId);
            if ((pojisteni.getPlatnostOd().isAfter(udalost.getDatumUdalosti())) || (pojisteni.getPlatnostDo().isBefore(udalost.getDatumUdalosti())))
                neplatnaPojisteniIds.add(pojisteniId);
        }
        return neplatnaPojisteniIds;
    }

    @Override
    public void remove(long udalostId) {
        PojistnaUdalostEntity fetchedEntity = getUdalostOrThrow(udalostId);
        udalostRepository.delete(fetchedEntity);
    }

}


