package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuranceRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuranceMapper;
import org.example.models.dto.mappers.InsuredMapper;
import org.example.models.exceptions.InsuranceNotFoundException;
import org.example.models.services.InsuredService;
import org.example.models.services.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.example.models.dto.Roles.POJISTNIK;

@Controller
@RequestMapping("/pojisteni/")
public class InsuranceController {

    @Autowired
    private InsuredMapper insuredMapper;
    @Autowired
    private InsuredService insuredService;

    @Autowired
    private InsuranceRepository insuranceRepository;

    @Autowired
    private InsuranceMapper insuranceMapper;
    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private UserRepository userRepository;

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("pojisteniAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuranceDTO> pojisteni = new ArrayList<>();
        if (user.isAdmin()) {
            pojisteni = insuranceService.getPojisteni(currentPage - 1);
        } else if (user.getRole().contains(POJISTNIK)) {
            pojisteni = insuranceService.getPojisteniByUserId(user.getUserId());
            model.addAttribute("paginace", 1);
        } else {
            pojisteni = insuranceService.getAllByPojistenecId(user.getPojistenec().getPojistenecId());
            model.addAttribute("paginace", 1);
        }
        model.addAttribute("pojisteni", pojisteni);
        if (user.isAdmin()) {
            if (insuranceRepository.count() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (insuranceRepository.count() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (insuranceRepository.count() > (currentPage * 10) + 10) {
                    model.addAttribute("prespristiStrana", currentPage + 2);
                }
            }
            if (currentPage > 1) {
                model.addAttribute("predchoziStrana", currentPage - 1);
                if (currentPage > 2) {
                    model.addAttribute("predpredchoziStrana", currentPage - 2);
                }
            }
        }
        return "pages/pojisteni/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("{pojistenecId}/novePojisteni")
    public String renderNovePojisteni(
            @PathVariable long pojistenecId, @ModelAttribute InsuranceDTO pojisteni,
            Model model
    ) {
        InsuredDTO pojistenec = insuredService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        model.addAttribute("pojisteniAktivni", 1);

        return "pages/pojisteni/novePojisteni";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("{pojistenecId}/novePojisteni")
    public String createNovePojisteni(
            @Valid @ModelAttribute InsuranceDTO pojisteni, BindingResult result, @PathVariable long pojistenecId, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderNovePojisteni(pojistenecId, pojisteni, model);
        if (pojisteni.getPlatnostDo().isBefore(pojisteni.getPlatnostOd())) {
            result.rejectValue("platnostDo", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNovePojisteni(pojistenecId, pojisteni, model);
        }
        pojisteni.setPojistenec(insuredService.getById(pojistenecId));
        insuranceService.create(pojisteni);
        redirectAttributes.addFlashAttribute("success", "Pojištění bylo vytvořeno.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("{pojistenecId}/delete/{pojisteniId}")
    public String deletePojisteni(@PathVariable long pojistenecId, @PathVariable long pojisteniId,
                                  RedirectAttributes redirectAttributes) {
        InsuranceEntity pojisteni = insuranceService.getPojisteniEntity(pojisteniId);
        if (pojisteni.getSeznamUdalosti().isEmpty()) {
            insuranceService.remove(pojisteniId);
            redirectAttributes.addFlashAttribute("success", "Pojištění bylo smazáno.");
        } else
            redirectAttributes.addFlashAttribute("error", "K pojištění se již váže alespoň jedna pojistná událost.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("{pojistenecId}/edit/{pojisteniId}")
    public String renderEditForm(
            @PathVariable Long pojistenecId, @PathVariable long pojisteniId,
            InsuranceDTO pojisteni, Model model
    ) {

        InsuranceDTO fetchedPojisteni = insuranceService.getById(pojisteniId);
        InsuredDTO pojistenec = insuredService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        insuranceMapper.updatePojisteniDTO(fetchedPojisteni, pojisteni);
        model.addAttribute("pojisteniAktivni", 1);
        return "pages/pojisteni/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("{pojistenecId}/edit/{pojisteniId}")
    public String editPojisteni(
            @PathVariable long pojistenecId, @PathVariable long pojisteniId,
            @Valid InsuranceDTO pojisteni,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (result.hasErrors())
            return renderEditForm(pojistenecId, pojisteniId, pojisteni, model);
        if (pojisteni.getPlatnostDo().isBefore(pojisteni.getPlatnostOd())) {
            result.rejectValue("platnostDo", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNovePojisteni(pojistenecId, pojisteni, model);
        }
        pojisteni.setPojisteniId(pojisteniId);
        pojisteni.setPojistenec(insuredService.getById(pojistenecId));
        insuranceService.edit(pojisteni);
        redirectAttributes.addFlashAttribute("success", "Aplikovatelné změny byly provedeny.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{pojistenecId}/detail/{pojisteniId}")
    public String renderDetail(
            @PathVariable long pojistenecId, @PathVariable long pojisteniId,
            Model model
    ) {
        InsuredDTO pojistenec = insuredService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        InsuranceDTO pojisteni = insuranceService.getById(pojisteniId);
        model.addAttribute("pojisteni", pojisteni);
        model.addAttribute("pojisteniAktivni", 1);

        return "pages/pojisteni/detail";
    }


    @ExceptionHandler({InsuranceNotFoundException.class})
    public String handlePojisteniNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištění nenalezeno.");
        return "redirect:/pojistenci/";
    }

}
