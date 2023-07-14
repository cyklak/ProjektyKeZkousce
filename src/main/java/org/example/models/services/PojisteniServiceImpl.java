package org.example.models.services;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojistenecRepository;
import org.example.data.repositories.PojisteniRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.mappers.PojisteniMapper;
import org.example.models.exceptions.PojisteniNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PojisteniServiceImpl implements PojisteniService {

    @Autowired
    private PojisteniRepository pojisteniRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PojistenecRepository pojistenecRepository;

    @Autowired
    private PojisteniMapper pojisteniMapper;

    @Override
    public void create(PojisteniDTO pojisteni) {
        PojisteniEntity newPojisteni = pojisteniMapper.toEntity(pojisteni);
        pojisteniRepository.save(newPojisteni);
    }

    @Override
    public List<PojisteniDTO> getAllByPojistenecId(Long pojistenecId) {
        List<PojisteniEntity> seznamPojisteni = pojistenecRepository.findById(pojistenecId).orElseThrow().getSeznamPojisteni();
        List<PojisteniDTO> pojisteniDTO = new ArrayList<>();
        for (PojisteniEntity pojisteni : seznamPojisteni) {
            pojisteniDTO.add(pojisteniMapper.toDTO(pojisteni));
        }
        return pojisteniDTO;
    }
    @Override
    public List<PojisteniDTO> getPojisteniByUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<PojistenecEntity> seznamPojistencu = pojistenecRepository.findAllByPojistnikId(userId);
        List<PojisteniEntity> result = new ArrayList<>();
        for (PojistenecEntity e : seznamPojistencu) {
            result.addAll(e.getSeznamPojisteni());}
        List<PojisteniDTO> result2 = new ArrayList<>();
        for (PojisteniEntity pojisteni: result){
            result2.add(pojisteniMapper.toDTO(pojisteni));}

        return result2;
    }

    @Override
    public PojisteniDTO getById(long pojisteniId) {
        PojisteniEntity fetchedPojisteni = getPojisteniOrThrow(pojisteniId);

        return pojisteniMapper.toDTO(fetchedPojisteni);
    }

    private PojisteniEntity getPojisteniOrThrow(long pojisteniID) {
        return pojisteniRepository
                .findById(pojisteniID)
                .orElseThrow(PojisteniNotFoundException::new);
    }

    public PojisteniEntity getPojisteniEntity(long pojisteniID) {
        return pojisteniRepository
                .findById(pojisteniID)
                .orElseThrow(PojisteniNotFoundException::new);
    }

    @Override
    public void remove(long pojisteniId) {
        PojisteniEntity fetchedEntity = getPojisteniOrThrow(pojisteniId);
        pojisteniRepository.delete(fetchedEntity);
    }

    @Override
    public void edit(PojisteniDTO pojisteni) {
        PojisteniEntity fetchedPojisteni = getPojisteniOrThrow(pojisteni.getPojisteniId());
        LocalDate puvodniPlatnostod = fetchedPojisteni.getPlatnostOd();
        LocalDate puvodniPlatnostDo = fetchedPojisteni.getPlatnostDo();
        pojisteniMapper.updatePojisteniEntity(pojisteni, fetchedPojisteni);
        if (puvodniPlatnostod.isBefore(LocalDate.now()) || puvodniPlatnostod.isEqual(LocalDate.now()))
            fetchedPojisteni.setPlatnostOd(puvodniPlatnostod);
        if (puvodniPlatnostDo.isBefore(LocalDate.now()))
            fetchedPojisteni.setPlatnostDo(puvodniPlatnostDo);
        pojisteniRepository.save(fetchedPojisteni);
    }

    @Override
    public List<PojisteniDTO> getPojisteni(int currentPage) {
        Page<PojisteniEntity> pageOfPojisteni = pojisteniRepository.findAll(PageRequest.of(currentPage, 10));
        List<PojisteniEntity> pojisteniEntities = pageOfPojisteni.getContent();
        List<PojisteniDTO> result = new ArrayList<>();
        for (PojisteniEntity e : pojisteniEntities) {
            result.add(pojisteniMapper.toDTO(e));
        }
        return result;
    }


}

