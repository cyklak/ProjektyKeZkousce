package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojisteniRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.mappers.PojistenecMapper;
import org.example.models.dto.mappers.PojisteniMapper;
import org.example.models.exceptions.PojisteniNotFoundException;
import org.example.models.services.PojistenecService;
import org.example.models.services.PojisteniService;
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

import static org.example.models.dto.Role.POJISTNIK;

@Controller
@RequestMapping("/pojisteni/")
public class PojisteniController {

    @Autowired
    private PojistenecMapper pojistenecMapper;
    @Autowired
    private PojistenecService pojistenecService;

    @Autowired
    private PojisteniRepository pojisteniRepository;

    @Autowired
    private PojisteniMapper pojisteniMapper;
    @Autowired
    private PojisteniService pojisteniService;

    @Autowired
    private UserRepository userRepository;

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("pojisteniAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PojisteniDTO> pojisteni = new ArrayList<>();
        if (user.isAdmin()) {
            pojisteni = pojisteniService.getPojisteni(currentPage - 1);
        } else if (user.getRole().contains(POJISTNIK)) {
            pojisteni = pojisteniService.getPojisteniByUserId(user.getUserId());
            model.addAttribute("paginace", 1);
        }
        else {pojisteni = pojisteniService.getAllByPojistenecId(user.getPojistenec().getPojistenecId());
            model.addAttribute("paginace", 1);}
        model.addAttribute("pojisteni", pojisteni);
        if (user.getRole().equals("ADMIN")) {
            if (pojisteniRepository.count() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (pojisteniRepository.count() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (pojisteniRepository.count() > (currentPage * 10) + 10) {
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
            @PathVariable long pojistenecId, @ModelAttribute PojisteniDTO pojisteni,
            Model model
    ) {
        PojistenecDTO pojistenec = pojistenecService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        model.addAttribute("pojisteniAktivni", 1);

        return "pages/pojisteni/novePojisteni";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("{pojistenecId}/novePojisteni")
    public String createNovePojisteni(
            @Valid @ModelAttribute PojisteniDTO pojisteni, BindingResult result, @PathVariable long pojistenecId, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderNovePojisteni(pojistenecId, pojisteni, model);
        if (pojisteni.getPlatnostDo().isBefore(pojisteni.getPlatnostOd())) {
            result.rejectValue("platnostDo", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNovePojisteni(pojistenecId, pojisteni, model);
        }
        pojisteni.setPojistenec(pojistenecService.getById(pojistenecId));
        pojisteniService.create(pojisteni);
        redirectAttributes.addFlashAttribute("success", "Pojištění bylo vytvořeno.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("{pojistenecId}/delete/{pojisteniId}")
    public String deletePojisteni(@PathVariable long pojistenecId, @PathVariable long pojisteniId,
                                  RedirectAttributes redirectAttributes) {
        PojisteniEntity pojisteni = pojisteniService.getPojisteniEntity(pojisteniId);
        if (pojisteni.getSeznamUdalosti().isEmpty()) {
            pojisteniService.remove(pojisteniId);
            redirectAttributes.addFlashAttribute("success", "Pojištění bylo smazáno.");
        } else
            redirectAttributes.addFlashAttribute("error", "K pojištění se již váže alespoň jedna pojistná událost.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("{pojistenecId}/edit/{pojisteniId}")
    public String renderEditForm(
            @PathVariable Long pojistenecId, @PathVariable long pojisteniId,
            PojisteniDTO pojisteni, Model model
    ) {

        PojisteniDTO fetchedPojisteni = pojisteniService.getById(pojisteniId);
        PojistenecDTO pojistenec = pojistenecService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        pojisteniMapper.updatePojisteniDTO(fetchedPojisteni, pojisteni);
        model.addAttribute("pojisteniAktivni", 1);
        return "pages/pojisteni/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("{pojistenecId}/edit/{pojisteniId}")
    public String editPojisteni(
            @PathVariable long pojistenecId, @PathVariable long pojisteniId,
            @Valid PojisteniDTO pojisteni,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model model
    ) {
        if (result.hasErrors())
            return renderEditForm(pojistenecId, pojisteniId, pojisteni, model);
        if (pojisteni.getPlatnostDo().isBefore(pojisteni.getPlatnostOd())) {
            result.rejectValue("platnostDo", "error", "Platnost do nemůže být menší než platnost od.");
            return renderNovePojisteni(pojistenecId, pojisteni, model);}
        pojisteni.setPojisteniId(pojisteniId);
        pojisteni.setPojistenec(pojistenecService.getById(pojistenecId));
        pojisteniService.edit(pojisteni);
        redirectAttributes.addFlashAttribute("success", "Aplikovatelné změny byly provedeny.");
        return "redirect:/pojistenci/{pojistenecId}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{pojistenecId}/detail/{pojisteniId}")
    public String renderDetail(
            @PathVariable long pojistenecId, @PathVariable long pojisteniId,
            Model model
    ) {
        PojistenecDTO pojistenec = pojistenecService.getById(pojistenecId);
        model.addAttribute("pojistenec", pojistenec);
        PojisteniDTO pojisteni = pojisteniService.getById(pojisteniId);
        model.addAttribute("pojisteni", pojisteni);
        model.addAttribute("pojisteniAktivni", 1);

        return "pages/pojisteni/detail";
    }


    @ExceptionHandler({PojisteniNotFoundException.class})
    public String handlePojisteniNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištění nenalezeno.");
        return "redirect:/pojistenci/";
    }

}
