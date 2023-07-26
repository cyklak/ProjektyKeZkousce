package org.example.models.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
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
/**
 * lombok generated constructor throws IllegalArgumentException that prevents null values in constructor arguments
 */
@AllArgsConstructor
@Service
public class InsuredService {

    @NonNull
    private final InsuredRepository insuredRepository;

    @NonNull
    private final InsuredMapper insuredMapper;

    @NonNull
    private final InsuranceEventRepository eventRepository;

    @NonNull
    private final InsuranceRepository insuranceRepository;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final UserService userService;



    /** creates a new insured person and saves them into InsuredRepository. If a policyholder creates a new insured person who is different from the policyholder a new user is also created
     * and a password is generated for them.
     * @param insured
     * @return
     */
    public String create(InsuredDTO insured) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuredEntity newInsured = insuredMapper.toEntity(insured);
        newInsured.setPolicyholderId(user.getUserId());
        if (insured.getEmail().equals(user.getEmail())) {
            newInsured.setUser(user);
            user.getRoles().add(INSURED);
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


    /**
     * @return DTOs of all items from InsuredRepository
     */
    public List<InsuredDTO> getAll() {
        return StreamSupport.stream(insuredRepository.findAll().spliterator(), false)
                .map(insuredMapper::toDTO)
                .toList();
    }

    /**
     * @return the number of all items in InsuredRepository
     */
    public Long getInsuredCount() {
        return insuredRepository.count();
    }


    /**
     * @param currentPage
     * @return DTOs of all items from InsuredRepository, 10 per page
     */
    public List<InsuredDTO> getInsuredList(int currentPage) {
        Page<InsuredEntity> pageOfPeople = insuredRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuredEntity> personEntities = pageOfPeople.getContent();
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : personEntities) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    /**
     * @param insuredId
     * @return DTO of a selected insured person
     */
    public InsuredDTO getById(long insuredId) {
        InsuredEntity fetchedPojistenec = getInsuredOrThrow(insuredId);

        return insuredMapper.toDTO(fetchedPojistenec);
    }

    /**
     * @param userId
     * @return list of DTOs of all insured people related to the current user, if they are a policyholder
     */
    public List<InsuredDTO> getInsuredListByUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<InsuredEntity> insuredList = insuredRepository.findAllBypolicyholderId(userId);
        List<InsuredDTO> result = new ArrayList<>();
        for (InsuredEntity e : insuredList) {
            result.add(insuredMapper.toDTO(e));
        }
        return result;
    }


    /** edits an insured person's details
     * @param insured
     */
    public void edit(InsuredDTO insured) {
        InsuredEntity fetchedInsured = getInsuredOrThrow(insured.getInsuredId());
        Long policyholderId = fetchedInsured.getPolicyholderId();
        List<InsuranceEventEntity> events = eventRepository.findAllByinsuredId(insured.getInsuredId());
        for (InsuranceEventEntity event : events) {
            event.setInsuredFirstName(insured.getFirstName());
            event.setInsuredLastName(insured.getLastName());
            eventRepository.save(event);
        }

        insuredMapper.updateInsuredEntity(insured, fetchedInsured);
        fetchedInsured.setPolicyholderId(policyholderId);
        insuredRepository.save(fetchedInsured);
    }

    /**
     * @param insuredID
     * @return an InsuredEntity selected by its ID
     */
    private InsuredEntity getInsuredOrThrow(long insuredID) {
        return insuredRepository
                .findById(insuredID)
                .orElseThrow(InsuredNotFoundException::new);
    }


    /** deletes a person from InsuredRepository
     * @param insuredId
     */
    public void remove(long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity entity = insuredRepository.findById(insuredId).orElseThrow().getUser();
        InsuredEntity fetchedEntity = getInsuredOrThrow(insuredId);
        List<InsuranceEventEntity> events = eventRepository.findAllByinsuredId(insuredId);
        for (InsuranceEventEntity event : events) {
            eventRepository.delete(event);
        }
        List<InsuranceEntity> insuranceList = insuranceRepository.findAllByinsured(fetchedEntity);
        for (InsuranceEntity insurance : insuranceList) {
            insuranceRepository.delete(insurance);
        }
        insuredRepository.delete(fetchedEntity);
        if (user.getUserId() == entity.getUserId()) {
            user.getRoles().remove(INSURED);
            userRepository.save(user);
        }
        if (user.getUserId() != entity.getUserId()) {
            userRepository.delete(entity);
        }

    }

}