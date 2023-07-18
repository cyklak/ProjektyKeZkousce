package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.InsuranceEventRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuranceEventDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuranceMapper;
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

import static org.example.models.dto.Roles.*;

@Controller
@RequestMapping("/udalosti/")
public class InsuranceEventController {

    private final InsuranceService insuranceService;

    private final InsuredService insuredService;

    private final InsuranceEventService udalostService;

    private final InsuranceEventMapper udalostMapper;

    public InsuranceEventController(InsuranceService insuranceService, InsuredService insuredService, InsuranceEventService udalostService, InsuranceEventMapper udalostMapper) {
        this.insuranceService = insuranceService;
        this.insuredService = insuredService;
        this.udalostService = udalostService;
        this.udalostMapper = udalostMapper;
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(
            @PathVariable int currentPage,
            Model model
    ) {
        model.addAttribute("udalostiAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuranceEventDTO> udalosti = new ArrayList<>();
        if (user.isAdmin()) {
            udalosti = udalostService.getUdalosti(currentPage - 1);

            if (udalostService.getEventCount() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (udalostService.getEventCount() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (udalostService.getEventCount() > (currentPage * 10) + 10) {
                    model.addAttribute("prespristiStrana", currentPage + 2);
                }
            }
            if (currentPage > 1) {
                model.addAttribute("predchoziStrana", currentPage - 1);
                if (currentPage > 2) {
                    model.addAttribute("predpredchoziStrana", currentPage - 2);
                }
            }
        } else if (user.getRole().contains(POJISTNIK)) {
            udalosti = udalostService.getUdalostiByUserId(user.getUserId());
            model.addAttribute("paginace", 1);
        } else {
            udalosti = udalostService.getUdalostibyPojistenecId(currentPage - 1, user.getPojistenec().getPojistenecId());
            model.addAttribute("paginace", 1);
        }
        model.addAttribute("udalosti", udalosti);
        List<InsuredDTO> pojistenci = new ArrayList<>();
        for (InsuranceEventDTO udalost : udalosti) {
            pojistenci.add(insuredService.getById(udalost.getPojistenecId()));
        }
        model.addAttribute("pojistenci", pojistenci);

        return "pages/udalosti/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{udalostId}/detail")
    public String renderDetail(@PathVariable long udalostId,
                               Model model
    ) {
        InsuranceEventDTO udalost = udalostService.getById(udalostId);
        List<InsuranceDTO> seznamPojisteni = udalostService.getInsurancesByEventId(udalostId);
        model.addAttribute("udalost", udalost);
        model.addAttribute("seznamPojisteni", seznamPojisteni);
        model.addAttribute("udalostiAktivni", 1);

        return "pages/udalosti/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @GetMapping("novaUdalost")
    public String renderNovaUdalost(
            InsuranceEventDTO udalost,
            Model model
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InsuredDTO pojistenec = insuredService.getById(user.getPojistenec().getPojistenecId());
        List<InsuranceDTO> pojisteni = insuranceService.getAllByPojistenecId(user.getPojistenec().getPojistenecId());

        if (udalost.getNeplatnaPojisteni() != null) {
            for (InsuranceDTO jednoPojisteni : pojisteni)
                if (udalost.getNeplatnaPojisteni().contains(jednoPojisteni.getPojisteniId())) {
                    jednoPojisteni.setAktivni(true);
                    udalost.getPojisteniIds().remove(jednoPojisteni.getPojisteniId());
                }
        }
        model.addAttribute("seznamPojisteni", pojisteni);
        model.addAttribute("pojistenec", pojistenec);
        model.addAttribute("udalostiAktivni", 1);
        return "pages/udalosti/novaUdalost";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @PostMapping("novaUdalost")
    public String createNovaUdalost(
            @Valid @ModelAttribute InsuranceEventDTO udalostDTO, BindingResult result, Model model,
            RedirectAttributes redirectAttributes
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors())
            return renderNovaUdalost(udalostDTO, model);
        List<Long> neplatnaPojisteni = udalostService.filterInsurances(udalostDTO);
        if (neplatnaPojisteni.isEmpty()) {
            udalostService.create(udalostDTO, user.getPojistenec().getPojistenecId());
            redirectAttributes.addFlashAttribute("success", "Pojistná událost byla vytvořena.");
            return "redirect:/udalosti/stranka/1";
        } else
            udalostDTO.setNeplatnaPojisteni(neplatnaPojisteni);
        return renderNovaUdalost(udalostDTO, model);

    }


    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @GetMapping("edit/{pojistnaUdalostId}")
    public String renderEditForm(
            @PathVariable long pojistnaUdalostId,
            InsuranceEventDTO udalostDTO, Model model
    ) {

        InsuranceEventDTO fetchedUdalost = udalostService.getById(pojistnaUdalostId);
        InsuredDTO pojistenec = insuredService.getById(udalostService.getById(pojistnaUdalostId).getPojistenecId());
        model.addAttribute("pojistenec", pojistenec);
        List<InsuranceDTO> pojisteni = insuranceService.getAllByPojistenecId(udalostService.getById(pojistnaUdalostId).getPojistenecId());
        if (udalostDTO.getNeplatnaPojisteni() != null) {
            for (InsuranceDTO jednopojisteni : pojisteni)
                if (udalostDTO.getNeplatnaPojisteni().contains(jednopojisteni.getPojisteniId()))
                    jednopojisteni.setAktivni(true);
        }

        model.addAttribute("seznamPojisteni", pojisteni);
        udalostMapper.updatePojistnaUdalostDTO(fetchedUdalost, udalostDTO);
        model.addAttribute("udalostiAktivni", 1);

        return "pages/udalosti/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @PostMapping("edit/{pojistnaUdalostId}")
    public String editPojisteni(
            @PathVariable long pojistnaUdalostId,
            @Valid InsuranceEventDTO udalostDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (result.hasErrors())
            return renderEditForm(pojistnaUdalostId, udalostDTO, model);
        udalostDTO.setPojistnaUdalostId(pojistnaUdalostId);
        List<Long> neplatnaPojisteni = udalostService.filterInsurances(udalostDTO);
        if (neplatnaPojisteni.isEmpty()) {
            udalostService.edit(udalostDTO, udalostService.getById(pojistnaUdalostId).getPojistenecId());
            redirectAttributes.addFlashAttribute("success", "Změny byly provedeny.");
            return "redirect:/udalosti/stranka/1";
        } else
            udalostDTO.setNeplatnaPojisteni(neplatnaPojisteni);
        return renderEditForm(pojistnaUdalostId, udalostDTO, model);

    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @GetMapping("delete/{udalostId}")
    public String deleteUdalost(@PathVariable long udalostId,
                                RedirectAttributes redirectAttributes) {
        udalostService.remove(udalostId);
        redirectAttributes.addFlashAttribute("success", "Událost byla smazána.");

        return "redirect:/udalosti/stranka/1";
    }

    @ExceptionHandler({EventNotFoundException.class})
    public String handleUdalostNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištěnec nenalezen.");
        return "redirect:/stranka/{currentPage}";
    }

}


