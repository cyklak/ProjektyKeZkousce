package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.InsuredRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuredMapper;
import org.example.models.exceptions.DuplicateEmailException;
import org.example.models.exceptions.InsuredNotFoundException;
import org.example.models.services.*;
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

import static org.example.models.dto.Roles.POJISTENY;
import static org.example.models.dto.Roles.POJISTNIK;

@Controller
@RequestMapping("/pojistenci/")
public class InsuredController {

    private final InsuredMapper insuredMapper;

    private final UserRepository userRepository;

    private final InsuredService insuredService;

    private final InsuredRepository insuredRepository;

    private final InsuranceService insuranceService;

    public InsuredController(InsuredMapper insuredMapper, UserRepository userRepository, InsuredService insuredService, InsuredRepository insuredRepository, InsuranceService insuranceService) {
        this.insuredMapper = insuredMapper;
        this.userRepository = userRepository;
        this.insuredService = insuredService;
        this.insuredRepository = insuredRepository;
        this.insuranceService = insuranceService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("pojistenciAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuredDTO> pojistenci = new ArrayList<>();
        if (user.isAdmin()) {
            pojistenci = insuredService.getPojistenci(currentPage - 1);
        } else if ((user.getRole().contains(POJISTNIK))) {
            pojistenci = insuredService.getPojistencibyUserId(user.getUserId());
            model.addAttribute("paginace", 1);
        } else {
            InsuredDTO pojisteny = insuredService.getById(user.getPojistenec().getPojistenecId());
            pojistenci.add(pojisteny);
        }
        model.addAttribute("pojistenci", pojistenci);
        if (user.isAdmin()) {
            if (insuredRepository.count() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (insuredRepository.count() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (insuredRepository.count() > (currentPage * 10) + 10) {
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
        return "pages/pojistenci/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("novyPojistenec")
    public String renderNovyPojistenec(@ModelAttribute InsuredDTO pojistenec, Model model) {
        model.addAttribute("pojistenciAktivni", 1);

        return "pages/pojistenci/novyPojistenec";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("novyPojistenec")
    public String createNovyPojistenec(
            @Valid @ModelAttribute InsuredDTO pojistenec,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String heslo = "";
        if (result.hasErrors())
            return renderNovyPojistenec(pojistenec, model);
        if ((user.getRole().contains(POJISTENY)) && (pojistenec.getEmail().equals(user.getEmail()))) {
            result.rejectValue("email", "error", "Již jste pojištěncem.");
            return renderNovyPojistenec(pojistenec, model);
        }
        if (!pojistenec.getEmail().equals(user.getEmail())) {
            try {
                heslo = insuredService.create(pojistenec);
            } catch (DuplicateEmailException e) {
                result.rejectValue("email", "error", "Email je již používán.");
                return renderNovyPojistenec(pojistenec, model);
            }
        }
        else {
            heslo = insuredService.create(pojistenec);
        }
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl vytvořen. Heslo je: " + heslo);

        return "redirect:/pojistenci/stranka/1";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{pojistenecId}")
    public String renderDetail(@PathVariable Long pojistenecId,
                               Model model
    ) {
        InsuredDTO pojistenec = insuredService.getById(pojistenecId);
        UserEntity pojistnik = userRepository.findById(pojistenec.getPojistnikId()).orElseThrow();
        model.addAttribute("pojistnik", pojistnik);
        model.addAttribute("pojistenec", pojistenec);
        List<InsuranceDTO> pojisteni = insuranceService.getAllByPojistenecId(pojistenecId);
        model.addAttribute("pojisteni", pojisteni);
        model.addAttribute("pojistenciAktivni", 1);

        return "pages/pojistenci/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("edit/{pojistenecId}")
    public String renderEditForm(
            @PathVariable Long pojistenecId,
            InsuredDTO pojistenec, Model model
    ) {
        InsuredDTO fetchedPojistenec = insuredService.getById(pojistenecId);
        insuredMapper.updatePojistenecDTO(fetchedPojistenec, pojistenec);
        model.addAttribute("pojistenciAktivni", 1);


        return "pages/pojistenci/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @PostMapping("edit/{pojistenecId}")
    public String editPojistenec(
            @PathVariable long pojistenecId,
            @Valid InsuredDTO pojistenec,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderEditForm(pojistenecId, pojistenec, model);

        pojistenec.setPojistenecId(pojistenecId);
        insuredService.edit(pojistenec);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl upraven.");

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "redirect:/pojistenci/stranka/1";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("delete/{pojistenecId}")
    public String deletePojistenec(@PathVariable long pojistenecId,
                                   RedirectAttributes redirectAttributes) {
        insuredService.remove(pojistenecId);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl smazán.");

        return "redirect:/pojistenci/stranka/1";
    }


    @ExceptionHandler({InsuredNotFoundException.class})
    public String handlePojistenecNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištěnec nenalezen.");
        return "redirect:/pojistenci/stranka/1";
    }

}
