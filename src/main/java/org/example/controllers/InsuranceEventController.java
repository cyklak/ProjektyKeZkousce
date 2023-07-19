package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.UserEntity;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuranceEventDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuranceEventMapper;
import org.example.models.exceptions.EventNotFoundException;
import org.example.models.services.InsuredService;
import org.example.models.services.InsuranceService;
import org.example.models.services.InsuranceEventService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.example.models.dto.Role.*;

@Controller
@RequestMapping("/udalosti/")
public class InsuranceEventController {

    private final InsuranceService insuranceService;

    private final InsuredService insuredService;

    private final InsuranceEventService eventService;

    private final InsuranceEventMapper eventMapper;

    public InsuranceEventController(InsuranceService insuranceService, InsuredService insuredService, InsuranceEventService eventService, InsuranceEventMapper eventMapper) {
        this.insuranceService = insuranceService;
        this.insuredService = insuredService;
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(
            @PathVariable int currentPage,
            Model model
    ) {
        model.addAttribute("eventsActive", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuranceEventDTO> events = new ArrayList<>();
        if (user.isAdmin()) {
            events = eventService.getEvents(currentPage - 1);

            if (eventService.getEventCount() > 10) {
                model.addAttribute("thisPage", currentPage);
            }
            if (eventService.getEventCount() > (currentPage * 10)) {
                model.addAttribute("nextPage", currentPage + 1);
                if (eventService.getEventCount() > (currentPage * 10) + 10) {
                    model.addAttribute("pageAfterNext", currentPage + 2);
                }
            }
            if (currentPage > 1) {
                model.addAttribute("lastPage", currentPage - 1);
                if (currentPage > 2) {
                    model.addAttribute("pageBeforeLast", currentPage - 2);
                }
            }
        } else {
            if (user.getRole().contains(POLICYHOLDER)) {
                events = eventService.getEventsByUserId(user.getUserId());}
            else {
                events = eventService.getEventsByInsuredId(user.getInsured().getInsuredId());}
            if (insuranceService.getAllByInsuredId(user.getUserId()).size()>0)
                model.addAttribute("newEventButton",1);
                model.addAttribute("pagination", 1);
        }
        model.addAttribute("events", events);

        return "pages/udalosti/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("{eventId}/detail")
    public String renderDetail(@PathVariable long eventId,
                               Model model
    ) {
        InsuranceEventDTO event = eventService.getById(eventId);
        List<InsuranceDTO> insuranceList = eventService.getInsurancesByEventId(eventId);
        model.addAttribute("event", event);
        model.addAttribute("insuranceList", insuranceList);
        model.addAttribute("eventsActive", 1);

        return "pages/udalosti/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @GetMapping("novaUdalost")
    public String renderNewEvent(
            InsuranceEventDTO event,
            Model model
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuredDTO insured = insuredService.getById(user.getInsured().getInsuredId());
        List<InsuranceDTO> insurances = insuranceService.getAllByInsuredId(user.getInsured().getInsuredId());

        if (event.getInvalidInsurances() != null) {
            for (InsuranceDTO insurance : insurances)
                if (event.getInvalidInsurances().contains(insurance.getInsuranceId())) {
                    insurance.setActive(true);
                    event.getInsuranceIds().remove(insurance.getInsuranceId());
                }
        }
        model.addAttribute("insuranceList", insurances);
        model.addAttribute("insured", insured);
        model.addAttribute("eventsActive", 1);
        return "pages/udalosti/novaUdalost";
    }

    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @PostMapping("novaUdalost")
    public String createNewEvent(
            @Valid @ModelAttribute InsuranceEventDTO eventDTO, BindingResult result, Model model,
            RedirectAttributes redirectAttributes
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors())
            return renderNewEvent(eventDTO, model);
        List<Long> invalidInsurances = eventService.filterInsurances(eventDTO);
        if (invalidInsurances.isEmpty()) {
            eventService.create(eventDTO, user.getInsured().getInsuredId());
            redirectAttributes.addFlashAttribute("success", "Pojistná událost byla vytvořena.");
            return "redirect:/udalosti/stranka/1";
        } else
            eventDTO.setInvalidInsurances(invalidInsurances);
        return renderNewEvent(eventDTO, model);

    }


    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @GetMapping("edit/{insuranceEventId}")
    public String renderEditForm(
            @PathVariable long insuranceEventId,
            InsuranceEventDTO eventDTO, Model model
    ) {

        InsuranceEventDTO fetchedEvent = eventService.getById(insuranceEventId);
        InsuredDTO insured = insuredService.getById(eventService.getById(insuranceEventId).getInsuredId());
        model.addAttribute("insured", insured);
        List<InsuranceDTO> insuranceList = insuranceService.getAllByInsuredId(eventService.getById(insuranceEventId).getInsuredId());
        if (eventDTO.getInvalidInsurances() != null) {
            for (InsuranceDTO insurance : insuranceList)
                if (eventDTO.getInvalidInsurances().contains(insurance.getInsuranceId()))
                    insurance.setActive(true);
        }

        model.addAttribute("insuranceList", insuranceList);
        eventMapper.updateInsuranceEventDTO(fetchedEvent, eventDTO);
        model.addAttribute("eventsActive", 1);

        return "pages/udalosti/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @PostMapping("edit/{insuranceEventId}")
    public String editEvent(
            @PathVariable long insuranceEventId,
            @Valid InsuranceEventDTO eventDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (result.hasErrors())
            return renderEditForm(insuranceEventId, eventDTO, model);
        eventDTO.setInsuranceEventId(insuranceEventId);
        List<Long> invalidInsurances = eventService.filterInsurances(eventDTO);
        if (invalidInsurances.isEmpty()) {
            eventService.edit(eventDTO, eventService.getById(insuranceEventId).getInsuredId());
            redirectAttributes.addFlashAttribute("success", "Změny byly provedeny.");
            return "redirect:/udalosti/stranka/1";
        } else
            eventDTO.setInvalidInsurances(invalidInsurances);
        return renderEditForm(insuranceEventId, eventDTO, model);

    }

    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @GetMapping("delete/{insuranceEventId}")
    public String deleteEvent(@PathVariable long insuranceEventId,
                                RedirectAttributes redirectAttributes) {
        eventService.remove(insuranceEventId);
        redirectAttributes.addFlashAttribute("success", "Událost byla smazána.");

        return "redirect:/udalosti/stranka/1";
    }

    @ExceptionHandler({EventNotFoundException.class})
    public String handleEventNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Událost nenalezena.");
        return "redirect:/udalosti/stranka/1";
    }

}


