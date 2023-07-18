package org.example.models.services;

import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.InsuranceEventEntity;
import org.example.data.entities.InsuredEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuredRepository;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.InsuranceEventRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuredMapper;
import org.example.models.exceptions.InsuredNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.example.models.dto.Roles.POJISTENY;

@Service
public class InsuredService {


    private final InsuredRepository insuredRepository;

    private final InsuredMapper insuredMapper;

    private final InsuranceEventRepository udalostRepository;

    private final InsuranceRepository insuranceRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public InsuredService(InsuredRepository insuredRepository, InsuredMapper insuredMapper, InsuranceEventRepository udalostRepository, InsuranceRepository insuranceRepository, UserRepository userRepository, UserService userService) {
        this.insuredRepository = insuredRepository;
        this.insuredMapper = insuredMapper;
        this.udalostRepository = udalostRepository;
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    public String create(InsuredDTO pojistenec) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuredEntity newPojistenec = insuredMapper.toEntity(pojistenec);
        newPojistenec.setPojistnikId(user.getUserId());
        if (pojistenec.getEmail().equals(user.getEmail())) {
            newPojistenec.setUserEntity(user);
            user.getRole().add(POJISTENY);
            insuredRepository.save(newPojistenec);
            userRepository.save(user);
        } else {
            String password = userService.generatePassword();
            UserEntity pojisteny = userService.createPojistenec(pojistenec, password);
            newPojistenec.setUserEntity(pojisteny);
            insuredRepository.save(newPojistenec);
            return password;
        }
        return "vaše současné heslo";
    }



    public List<InsuredDTO> getAll() {
        return StreamSupport.stream(insuredRepository.findAll().spliterator(), false)
                .map(i -> insuredMapper.toDTO(i))
                .toList();
    }

    public Long getInsuredCount() {
       return insuredRepository.count();
    }


    public List<InsuredDTO> getPojistenci(int currentPage) {
        Page<InsuredEntity> pageOfPeople = insuredRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuredEntity> personEntities = pageOfPeople.getContent();
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : personEntities) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    public InsuredDTO getById(long pojistenecId) {
        InsuredEntity fetchedPojistenec = getPojistenecOrThrow(pojistenecId);

        return insuredMapper.toDTO(fetchedPojistenec);
    }

    public List<InsuredDTO> getPojistencibyUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<InsuredEntity> seznamPojistencu = insuredRepository.findAllByPojistnikId(userId);
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : seznamPojistencu) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    public void edit(InsuredDTO pojistenec) {
        InsuredEntity fetchedPojistenec = getPojistenecOrThrow(pojistenec.getPojistenecId());
        List<InsuranceEventEntity> udalosti = udalostRepository.findAllBypojistenecId(pojistenec.getPojistenecId());
        for (InsuranceEventEntity udalost : udalosti) {
            udalost.setJmenoPojisteneho(pojistenec.getJmeno());
            udalost.setPrijmeniPojisteneho(pojistenec.getPrijmeni());
            udalostRepository.save(udalost);
        }

        insuredMapper.updatePojistenecEntity(pojistenec, fetchedPojistenec);
        insuredRepository.save(fetchedPojistenec);
    }

    private InsuredEntity getPojistenecOrThrow(long pojistenecID) {
        return insuredRepository
                .findById(pojistenecID)
                .orElseThrow(InsuredNotFoundException::new);
    }


    public void remove(long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity entity = insuredRepository.findById(pojistenecId).orElseThrow().getUserEntity();
        InsuredEntity fetchedEntity = getPojistenecOrThrow(pojistenecId);
        List<InsuranceEventEntity> udalosti = udalostRepository.findAllBypojistenecId(pojistenecId);
        for (InsuranceEventEntity udalost: udalosti) {
            udalostRepository.delete(udalost);
        }
        List<InsuranceEntity> seznamPojisteni = insuranceRepository.findAllBypojistenec(fetchedEntity);
        for (InsuranceEntity pojisteni: seznamPojisteni) {
            insuranceRepository.delete(pojisteni);
        }
        insuredRepository.delete(fetchedEntity);
        if (user.getUserId() == entity.getUserId()) {
            user.getRole().remove(POJISTENY);
            userRepository.save(user);
        }
        if (user.getUserId() != entity.getUserId()) {
            userRepository.delete(entity);
        }

    }

}