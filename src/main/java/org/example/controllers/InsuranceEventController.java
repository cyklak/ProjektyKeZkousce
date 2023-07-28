package org.example.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
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

/**
 * lombok generated constructor throws IllegalArgumentException that prevents null values in constructor arguments
 */
@AllArgsConstructor
@Controller
@RequestMapping("/udalosti/")
public class InsuranceEventController {

    @NonNull
    private final InsuranceService insuranceService;

    @NonNull
    private final InsuredService insuredService;

    @NonNull
    private final InsuranceEventService eventService;

    @NonNull
    private final InsuranceEventMapper eventMapper;

    /**
     * @param model
     * @param currentPage if user is admin, 10 items per page are displayed
     * @return list of all insurance events related to the current user; if user is admin, list of all events in InsuranceEventRepo is fetched
     */
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
            if (user.getRoles().contains(POLICYHOLDER)) {
                events = eventService.getEventsByUserId(user.getUserId());
            } else {
                events = eventService.getEventsByInsuredId(user.getInsured().getInsuredId());
            }
            if (user.getRoles().contains(INSURED)) {
                if (insuranceService.getAllByInsuredId(user.getInsured().getInsuredId()).size() > 0)
                    model.addAttribute("newEventButton", 1);
                model.addAttribute("pagination", 1);
            }
        }
        model.addAttribute("events", events);

        return "pages/udalosti/index";
    }

    /**
     * this method shows details of an insurance event
     *
     * @param eventId
     * @param model
     * @return
     */
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

    /**
     * @param event
     * @param model
     * @return a new insurance event form
     */
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

    /**
     * creates a new insurance event
     *
     * @param eventDTO
     * @param result
     * @param model
     * @param redirectAttributes
     * @return
     */
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


    /**
     * @param insuranceEventId
     * @param eventDTO
     * @param model
     * @return an insurance event edit form
     */
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

    /**
     * edits a selected insurance event
     *
     * @param insuranceEventId
     * @param eventDTO
     * @param result
     * @param redirectAttributes
     * @param model
     * @return
     */
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

    /**
     * deletes a selected insurance event
     *
     * @param insuranceEventId
     * @param redirectAttributes
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_INSURED"})
    @GetMapping("delete/{insuranceEventId}")
    public String deleteEvent(@PathVariable long insuranceEventId,
                              RedirectAttributes redirectAttributes) {
        eventService.remove(insuranceEventId);
        redirectAttributes.addFlashAttribute("success", "Událost byla smazána.");

        return "redirect:/udalosti/stranka/1";
    }

    /**
     * if user tries to fetch a non-existing event from InsuranceEventRepository, this method displays an error message
     *
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler({EventNotFoundException.class})
    public String handleEventNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Událost nenalezena.");
        return "redirect:/udalosti/stranka/1";
    }

}


