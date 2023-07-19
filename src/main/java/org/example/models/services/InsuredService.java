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

import static org.example.models.dto.Role.INSURED;

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


    public String create(InsuredDTO insured) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuredEntity newInsured = insuredMapper.toEntity(insured);
        newInsured.setPolicyholderId(user.getUserId());
        if (insured.getEmail().equals(user.getEmail())) {
            newInsured.setUser(user);
            user.getRole().add(INSURED);
            insuredRepository.save(newInsured);
            userRepository.save(user);
        } else {
            String password = userService.generatePassword();
            UserEntity insuredEntity = userService.createInsured(insured, password);
            newInsured.setUser(insuredEntity);
            insuredRepository.save(newInsured);
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


    public List<InsuredDTO> getInsuredList(int currentPage) {
        Page<InsuredEntity> pageOfPeople = insuredRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuredEntity> personEntities = pageOfPeople.getContent();
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : personEntities) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    public InsuredDTO getById(long insuredId) {
        InsuredEntity fetchedPojistenec = getInsuredOrThrow(insuredId);

        return insuredMapper.toDTO(fetchedPojistenec);
    }

    public List<InsuredDTO> getInsuredListByUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<InsuredEntity> insuredList = insuredRepository.findAllBypolicyholderId(userId);
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : insuredList) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    public void edit(InsuredDTO insured) {
        InsuredEntity fetchedInsured = getInsuredOrThrow(insured.getInsuredId());
        Long policyholderId = fetchedInsured.getPolicyholderId();
        List<InsuranceEventEntity> events = udalostRepository.findAllByinsuredId(insured.getInsuredId());
        for (InsuranceEventEntity event : events) {
            event.setInsuredFirstName(insured.getFirstName());
            event.setInsuredLastName(insured.getLastName());
            udalostRepository.save(event);
        }

        insuredMapper.updateInsuredEntity(insured, fetchedInsured);
        fetchedInsured.setPolicyholderId(policyholderId);
        insuredRepository.save(fetchedInsured);
    }

    private InsuredEntity getInsuredOrThrow(long insuredID) {
        return insuredRepository
                .findById(insuredID)
                .orElseThrow(InsuredNotFoundException::new);
    }


    public void remove(long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity entity = insuredRepository.findById(insuredId).orElseThrow().getUser();
        InsuredEntity fetchedEntity = getInsuredOrThrow(insuredId);
        List<InsuranceEventEntity> events = udalostRepository.findAllByinsuredId(insuredId);
        for (InsuranceEventEntity event : events) {
            udalostRepository.delete(event);
        }
        List<InsuranceEntity> insuranceList = insuranceRepository.findAllByinsured(fetchedEntity);
        for (InsuranceEntity insurance : insuranceList) {
            insuranceRepository.delete(insurance);
        }
        insuredRepository.delete(fetchedEntity);
        if (user.getUserId() == entity.getUserId()) {
            user.getRole().remove(INSURED);
            userRepository.save(user);
        }
        if (user.getUserId() != entity.getUserId()) {
            userRepository.delete(entity);
        }

    }

}