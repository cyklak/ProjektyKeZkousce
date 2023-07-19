package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.UserEntity;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuranceMapper;
import org.example.models.exceptions.InsuranceNotFoundException;
import org.example.models.services.InsuredService;
import org.example.models.services.InsuranceService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.example.models.dto.Role.POLICYHOLDER;

@Controller
@RequestMapping("/pojisteni/")
public class InsuranceController {

    private final InsuredService insuredService;

    private final InsuranceMapper insuranceMapper;

    private final InsuranceService insuranceService;


    public InsuranceController(InsuredService insuredService, InsuranceMapper insuranceMapper, InsuranceService insuranceService) {
        this.insuredService = insuredService;
        this.insuranceMapper = insuranceMapper;
        this.insuranceService = insuranceService;
    }


    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("insuranceActive", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuranceDTO> insurances = new ArrayList<>();
        if (user.isAdmin()) {
            insurances = insuranceService.getInsurances(currentPage - 1);
        } else if (user.getRole().contains(POLICYHOLDER)) {
            insurances = insuranceService.getInsurancesByUserId(user.getUserId());
            model.addAttribute("pagination", 1);
        } else {
            insurances = insuranceService.getAllByInsuredId(user.getInsured().getInsuredId());
            model.addAttribute("pagination", 1);
        }
        model.addAttribute("insurances", insurances);
        if (user.isAdmin()) {
            if (insuranceService.getInsuranceCount() > 10) {
                model.addAttribute("thisPage", currentPage);
            }
            if (insuranceService.getInsuranceCount() > (currentPage * 10)) {
                model.addAttribute("nextPage", currentPage + 1);
                if (insuranceService.getInsuranceCount() > (currentPage * 10) + 10) {
                    model.addAttribute("pageAfterNext", currentPage + 2);
                }
            }
            if (currentPage > 1) {
                model.addAttribute("previousPage", currentPage - 1);
                if (currentPage > 2) {
                    model.addAttribute("pageBeforeLast", currentPage - 2);
                }
            }
        }
        return "pages/pojisteni/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @GetMapping("{insuredId}/novePojisteni")
    public String renderNewInsurance(
            @PathVariable long insuredId, @ModelAttribute InsuranceDTO insurance,
            Model model
    ) {
        InsuredDTO insured = insuredService.getById(insuredId);
        model.addAttribute("insured", insured);
        model.addAttribute("insuranceActive", 1);

        return "pages/pojisteni/novePojisteni";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @PostMapping("{insuredId}/novePojisteni")
    public String createNewInsurance(
            @Valid @ModelAttribute InsuranceDTO insurance, BindingResult result, @PathVariable long insuredId, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderNewInsurance(insuredId, insurance, model);
        if (insurance.getValidUntil().isBefore(insurance.getValidFrom())) {
            result.rejectValue("validUntil", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNewInsurance(insuredId, insurance, model);
        }
        insurance.setInsured(insuredService.getById(insuredId));
        insuranceService.create(insurance);
        redirectAttributes.addFlashAttribute("success", "Pojištění bylo vytvořeno.");
        return "redirect:/pojistenci/{insuredId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @GetMapping("{insuredId}/delete/{insuranceId}")
    public String deletePojisteni(@PathVariable long insuredId, @PathVariable long insuranceId,
                                  RedirectAttributes redirectAttributes) {
        InsuranceEntity insurance = insuranceService.getPojisteniEntity(insuranceId);
        if (insurance.getEventList().isEmpty()) {
            insuranceService.remove(insuranceId);
            redirectAttributes.addFlashAttribute("success", "Pojištění bylo smazáno.");
        } else
            redirectAttributes.addFlashAttribute("error", "K pojištění se již váže alespoň jedna pojistná událost.");
        return "redirect:/pojistenci/{insuredId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @GetMapping("{insuredId}/edit/{insuranceId}")
    public String renderEditForm(
            @PathVariable Long insuredId, @PathVariable long insuranceId,
            InsuranceDTO insurance, Model model
    ) {

        InsuranceDTO fetchedInsurance = insuranceService.getById(insuranceId);
        InsuredDTO insured = insuredService.getById(insuredId);
        model.addAttribute("insured", insured);
        insuranceMapper.updateInsuranceDTO(fetchedInsurance, insurance);
        model.addAttribute("insuranceActive", 1);
        return "pages/pojisteni/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @PostMapping("{insuredId}/edit/{insuranceId}")
    public String editPojisteni(
            @PathVariable long insuredId, @PathVariable long insuranceId,
            @Valid InsuranceDTO insurance,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (result.hasErrors())
            return renderEditForm(insuredId, insuranceId, insurance, model);
        if (insurance.getValidUntil().isBefore(insurance.getValidFrom())) {
            result.rejectValue("validUntil", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNewInsurance(insuredId, insurance, model);
        }
        insurance.setInsuranceId(insuranceId);
        insurance.setInsured(insuredService.getById(insuredId));
        insuranceService.edit(insurance);
        redirectAttributes.addFlashAttribute("success", "Aplikovatelné změny byly provedeny.");
        return "redirect:/pojistenci/{insuredId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("{insuredId}/detail/{insuranceId}")
    public String renderDetail(
            @PathVariable long insuredId, @PathVariable long insuranceId,
            Model model
    ) {
        InsuredDTO insured = insuredService.getById(insuredId);
        model.addAttribute("insured", insured);
        InsuranceDTO insurance = insuranceService.getById(insuranceId);
        model.addAttribute("insurance", insurance);
        model.addAttribute("insuranceActive", 1);

        return "pages/pojisteni/detail";
    }


    @ExceptionHandler({InsuranceNotFoundException.class})
    public String handleInsuranceNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištění nenalezeno.");
        return "redirect:/pojisteni/stranka/1";
    }

}
