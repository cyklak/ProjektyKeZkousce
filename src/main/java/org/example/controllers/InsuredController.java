package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.UserEntity;
import org.example.models.dto.InsuranceDTO;
import org.example.models.dto.InsuredDTO;
import org.example.models.dto.mappers.InsuredMapper;
import org.example.models.exceptions.DuplicateEmailException;
import org.example.models.exceptions.InsuredNotFoundException;
import org.example.models.services.*;
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
@RequestMapping("/pojistenci/")
public class InsuredController {

    private final InsuredMapper insuredMapper;

    private final UserService userService;

    private final InsuredService insuredService;

    private final InsuranceService insuranceService;

    public InsuredController(InsuredMapper insuredMapper, UserService userService, InsuredService insuredService, InsuranceService insuranceService) {
        this.insuredMapper = insuredMapper;
        this.userService = userService;
        this.insuredService = insuredService;
        this.insuranceService = insuranceService;
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("insuredActive", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<InsuredDTO> insuredList = new ArrayList<>();
        if (user.isAdmin()) {
            insuredList = insuredService.getInsuredList(currentPage - 1);
        } else if ((user.getRole().contains(POLICYHOLDER))) {
            insuredList = insuredService.getInsuredListByUserId(user.getUserId());
            model.addAttribute("pagination", 1);
        } else {
            InsuredDTO insured = insuredService.getById(user.getInsured().getInsuredId());
            insuredList.add(insured);
        }
        model.addAttribute("insuredList", insuredList);
        if (user.isAdmin()) {
            if (insuredService.getInsuredCount() > 10) {
                model.addAttribute("thisPage", currentPage);
            }
            if (insuredService.getInsuredCount() > (currentPage * 10)) {
                model.addAttribute("nextPage", currentPage + 1);
                if (insuredService.getInsuredCount() > (currentPage * 10) + 10) {
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
        return "pages/pojistenci/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @GetMapping("novyPojistenec")
    public String renderNewInsured(@ModelAttribute InsuredDTO insured, Model model) {
        model.addAttribute("insuredActive", 1);

        return "pages/pojistenci/novyPojistenec";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @PostMapping("novyPojistenec")
    public String createNewInsured(
            @Valid @ModelAttribute InsuredDTO insured,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String password = "";
        if (result.hasErrors())
            return renderNewInsured(insured, model);
        if ((user.getRole().contains(INSURED)) && (insured.getEmail().equals(user.getEmail()))) {
            result.rejectValue("email", "error", "Již jste pojištěncem.");
            return renderNewInsured(insured, model);
        }
        if (!insured.getEmail().equals(user.getEmail())) {
            try {
                password = insuredService.create(insured);
            } catch (DuplicateEmailException e) {
                result.rejectValue("email", "error", "Email je již používán.");
                return renderNewInsured(insured, model);
            }
        }
        else {
            password = insuredService.create(insured);
        }
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl vytvořen. Heslo je: " + password);

        return "redirect:/pojistenci/stranka/1";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("{insuredId}")
    public String renderDetail(@PathVariable Long insuredId,
                               Model model
    ) {
        InsuredDTO insured = insuredService.getById(insuredId);
        String email = userService.getPolicyholderEmail(insured.getPolicyholderId());
        model.addAttribute("email", email);
        model.addAttribute("insured", insured);
        List<InsuranceDTO> insuranceList = insuranceService.getAllByInsuredId(insuredId);
        model.addAttribute("insuranceList", insuranceList);
        model.addAttribute("insuredActive", 1);

        return "pages/pojistenci/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("edit/{insuredId}")
    public String renderEditForm(
            @PathVariable Long insuredId,
            InsuredDTO insured, Model model
    ) {
        InsuredDTO fetchedInsured = insuredService.getById(insuredId);
        insuredMapper.updateInsuredDTO(fetchedInsured, insured);
        model.addAttribute("insuredActive", 1);


        return "pages/pojistenci/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @PostMapping("edit/{insuredId}")
    public String editInsured(
            @PathVariable long insuredId,
            @Valid InsuredDTO insured,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderEditForm(insuredId, insured, model);

        insured.setInsuredId(insuredId);
        insuredService.edit(insured);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl upraven.");

        return "redirect:/pojistenci/stranka/1";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER"})
    @GetMapping("delete/{insuredId}")
    public String deleteInsured(@PathVariable long insuredId,
                                   RedirectAttributes redirectAttributes) {
        insuredService.remove(insuredId);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl smazán.");

        return "redirect:/pojistenci/stranka/1";
    }


    @ExceptionHandler({InsuredNotFoundException.class})
    public String handleInsuredNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištěnec nenalezen.");
        return "redirect:/pojistenci/stranka/1";
    }

}
