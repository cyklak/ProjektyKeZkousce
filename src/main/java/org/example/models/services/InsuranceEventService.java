package org.example.models.services;

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

@Service
public class InsuranceEventService {


    private final InsuranceRepository insuranceRepository;


    private final InsuredRepository insuredRepository;


    private final InsuranceMapper insuranceMapper;


    private final InsuranceService insuranceService;

    private final InsuranceEventMapper udalostMapper;


    private final InsuranceEventRepository eventRepository;


    public InsuranceEventService(InsuranceRepository insuranceRepository, InsuredRepository insuredRepository, InsuranceMapper insuranceMapper, InsuranceService insuranceService, InsuranceEventMapper udalostMapper, InsuranceEventRepository eventRepository) {
        this.insuranceRepository = insuranceRepository;
        this.insuredRepository = insuredRepository;
        this.insuranceMapper = insuranceMapper;
        this.insuranceService = insuranceService;
        this.udalostMapper = udalostMapper;
        this.eventRepository = eventRepository;
    }


    public void create(InsuranceEventDTO event, Long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity eventEntity = udalostMapper.toEntity(event);
        eventEntity.setInsuredId(insuredId);
        eventEntity.setInsurances(new ArrayList<>());
        for (Long pojisteniId : event.getInsuranceIds()) {
            eventEntity.getInsurances().add(insuranceService.getPojisteniEntity(pojisteniId));
        }
        if (user.getRole().contains(Role.POLICYHOLDER))
            eventEntity.setPolicyholderId(user.getUserId());
        else
            eventEntity.setPolicyholderId(user.getInsured().getPolicyholderId());
        eventEntity.setInsuredFirstName(insuredRepository.findById(insuredId).get().getFirstName());
        eventEntity.setInsuredLastName(insuredRepository.findById(insuredId).get().getLastName());
        eventRepository.save(eventEntity);

    }


    public List<InsuranceEventDTO> getEvents(int currentPage) {
        Page<InsuranceEventEntity> pageOfPeople = eventRepository.findAll(PageRequest.of(currentPage, 10));
        List<InsuranceEventEntity> eventEntities = pageOfPeople.getContent();
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : eventEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }

    public Long getEventCount() {
        return eventRepository.count();
    }


    public InsuranceEventDTO getById(long eventId) {
        InsuranceEventEntity fetchedEvent = getUdalostOrThrow(eventId);

        return udalostMapper.toDTO(fetchedEvent);
    }

    private InsuranceEventEntity getUdalostOrThrow(long eventID) {
        return eventRepository
                .findById(eventID)
                .orElseThrow(EventNotFoundException::new);
    }


    public List<InsuranceEventDTO> getEventsByInsuredId(Long insuredId) {
        List<InsuranceEventEntity> eventEntities = eventRepository.findAllByinsuredId(insuredId);
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity e : eventEntities) {
            result.add(udalostMapper.toDTO(e));
        }
        return result;
    }


    public List<InsuranceEventDTO> getEventsByUserId(long userId) {
        List<InsuranceEventEntity> eventList = eventRepository.findAllBypolicyholderId(userId);
        List<InsuranceEventDTO> result = new ArrayList<>();
        for (InsuranceEventEntity event : eventList) {
            result.add(udalostMapper.toDTO(event));
        }
        return result;
    }

    public List<InsuranceDTO> getInsurancesByEventId(Long eventId) {
        InsuranceEventEntity event = getUdalostOrThrow(eventId);
        List<InsuranceDTO> insurances = new ArrayList<>();
        for (InsuranceEntity insurance : event.getInsurances()) {
            insurances.add(insuranceMapper.toDTO(insurance));
        }
        return insurances;
    }


    public void edit(InsuranceEventDTO eventDTO, Long insuredId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuranceEventEntity fetchedEvent = getUdalostOrThrow(eventDTO.getInsuranceEventId());
        udalostMapper.updateInsuranceEventEntity(eventDTO, fetchedEvent);
        List<InsuranceEntity> insurances = new ArrayList<>();
        for (Long insuranceId : eventDTO.getInsuranceIds()) {
            insurances.add(insuranceRepository.findById(insuranceId).orElseThrow());
        }
        fetchedEvent.setInsurances(insurances);
        fetchedEvent.setInsuredId(insuredId);
        fetchedEvent.setInsuredFirstName(insuredRepository.findById(insuredId).get().getFirstName());
        fetchedEvent.setInsuredLastName(insuredRepository.findById(insuredId).get().getLastName());
        if (user.getRole().contains(Role.POLICYHOLDER))
            fetchedEvent.setPolicyholderId(user.getUserId());
        else
            fetchedEvent.setPolicyholderId(user.getInsured().getPolicyholderId());
        eventRepository.save(fetchedEvent);

    }


    public List<Long> filterInsurances(InsuranceEventDTO event) {
        List<Long> invalidInsuranceIds = new ArrayList<>();
        for (Long insuranceId : event.getInsuranceIds()) {
            InsuranceDTO insurance = insuranceService.getById(insuranceId);
            if ((insurance.getValidFrom().isAfter(event.getDateOfEvent())) || (insurance.getValidUntil().isBefore(event.getDateOfEvent())))
                invalidInsuranceIds.add(insuranceId);
        }
        return invalidInsuranceIds;
    }


    public void remove(long eventId) {
        InsuranceEventEntity fetchedEntity = getUdalostOrThrow(eventId);
        eventRepository.delete(fetchedEntity);
    }

}


