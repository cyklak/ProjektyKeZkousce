package org.example.models.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.InsuranceEventEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuredRepository;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.InsuranceEventRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuranceEventDTO;
import org.example.models.dto.Role;
import org.example.models.dto.mappers.InsuranceMapper;
import org.example.models.dto.mappers.InsuranceEventMapper;
import org.example.models.exceptions.EventNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * lombok generated constructor throws IllegalArgumentException that prevents null values in constructor arguments
 */
@AllArgsConstructor
@Service
public class InsuranceEventService {

    @NonNull
    private final InsuranceRepository insuranceRepository;

    @NonNull
    private final InsuredRepository insuredRepository;

    @NonNull
    private final InsuranceMapper insuranceMapper;

    @NonNull
    private final InsuranceService insuranceService;

    @NonNull
    private final InsuranceEventMapper eventMapper;

    @NonNull
    private final InsuranceEventRepository eventRepository;


    /** creates a new insurance event
     * @param event
     * @param insuredId
     */
    public void create(InsuranceEventDTO event, Long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity eventEntity = eventMapper.toEntity(event);
        eventEntity.setInsuredId(insuredId);
        eventEntity.setInsurances(new ArrayList<>());
        for (Long pojisteniId : event.getInsuranceIds()) {
            eventEntity.getInsurances().add(insuranceService.getInsuranceOrThrow(pojisteniId));
        }
        if (user.getRoles().contains(Role.POLICYHOLDER))
            eventEntity.setPolicyholderId(user.getUserId());
        else
            eventEntity.setPolicyholderId(user.getInsured().getPolicyholderId());
        eventEntity.setInsuredFirstName(insuredRepository.findById(insuredId).get().getFirstName());
        eventEntity.setInsuredLastName(insuredRepository.findById(insuredId).get().getLastName());
        eventRepository.save(eventEntity);

    }


    /**
     * @param currentPage
     * @return pages of 10 items of all events in eventRepository
     */
    public List<InsuranceEventDTO> getEvents(int currentPage) {
        Page<InsuranceEventEntity> pageOfPeople = eventRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuranceEventEntity> eventEntities = pageOfPeople.getContent();
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : eventEntities) {
            result.add(eventMapper.toDTO(e));
        }
        return result;
    }

    /**
     * @return number of events in eventRepository
     */
    public Long getEventCount() {
        return eventRepository.count();
    }


    /**
     * @param eventId
     * @return insurance event DTO by its ID
     */
    public InsuranceEventDTO getById(long eventId) {
        InsuranceEventEntity fetchedEvent = getUdalostOrThrow(eventId);

        return eventMapper.toDTO(fetchedEvent);
    }

    /**
     * @param eventID
     * @return insurance event Entity by its ID
     */
    private InsuranceEventEntity getUdalostOrThrow(long eventID) {
        return eventRepository
                .findById(eventID)
                .orElseThrow(EventNotFoundException::new);
    }


    /**
     * @param insuredId
     * @return all insurance events of one insured person
     */
    public List<InsuranceEventDTO> getEventsByInsuredId(Long insuredId) {
        List<InsuranceEventEntity> eventEntities = eventRepository.findAllByinsuredId(insuredId);
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : eventEntities) {
            result.add(eventMapper.toDTO(e));
        }
        return result;
    }


    /**
     * @param userId
     * @return all insurance events related to insurances paid for by one policyholder
     */
    public List<InsuranceEventDTO> getEventsByUserId(long userId) {
        List<InsuranceEventEntity> eventList = eventRepository.findAllBypolicyholderId(userId);
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity event : eventList) {
            result.add(eventMapper.toDTO(event));
        }
        return result;
    }

    /**
     * @param eventId
     * @return DTOs of all insurances related to a specific insurance
     */
    public List<InsuranceDTO> getInsurancesByEventId(Long eventId) {
        InsuranceEventEntity event = getUdalostOrThrow(eventId);
        List<InsuranceDTO> insurances = new ArrayList<>();
        for (InsuranceEntity insurance : event.getInsurances()) {
            insurances.add(insuranceMapper.toDTO(insurance));
        }
        return insurances;
    }


    /** edits a selected insurance event
     * @param eventDTO
     * @param insuredId
     */
    public void edit(InsuranceEventDTO eventDTO, Long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity fetchedEvent = getUdalostOrThrow(eventDTO.getInsuranceEventId());
        eventMapper.updateInsuranceEventEntity(eventDTO, fetchedEvent);
        List<InsuranceEntity> insurances = new ArrayList<>();
        for (Long insuranceId : eventDTO.getInsuranceIds()) {
            insurances.add(insuranceRepository.findById(insuranceId).orElseThrow());
        }
        fetchedEvent.setInsurances(insurances);
        fetchedEvent.setInsuredId(insuredId);
        fetchedEvent.setInsuredFirstName(insuredRepository.findById(insuredId).get().getFirstName());
        fetchedEvent.setInsuredLastName(insuredRepository.findById(insuredId).get().getLastName());
        if (user.getRoles().contains(Role.POLICYHOLDER))
            fetchedEvent.setPolicyholderId(user.getUserId());
        else
            fetchedEvent.setPolicyholderId(user.getInsured().getPolicyholderId());
        eventRepository.save(fetchedEvent);

    }


    /**
     * @param event
     * @return Ids of all insurances which cannot be applied to a selected insurance event
     */
    public List<Long> filterInsurances(InsuranceEventDTO event) {
        List<Long> invalidInsuranceIds = new ArrayList<>();
        for (Long insuranceId : event.getInsuranceIds()) {
            InsuranceDTO insurance = insuranceService.getById(insuranceId);
            if ((insurance.getValidFrom().isAfter(event.getDateOfEvent())) || (insurance.getValidUntil().isBefore(event.getDateOfEvent())))
                invalidInsuranceIds.add(insuranceId);
        }
        return invalidInsuranceIds;
    }


    /** deletes an event from eventRepository
     * @param eventId
     */
    public void remove(long eventId) {
        InsuranceEventEntity fetchedEntity = getUdalostOrThrow(eventId);
        eventRepository.delete(fetchedEntity);
    }

}


