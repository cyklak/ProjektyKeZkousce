package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojisteniRepository;
import org.example.data.repositories.PojistnaUdalostRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;
import org.example.models.dto.mappers.PojisteniMapper;
import org.example.models.dto.mappers.PojistnaUdalostMapper;
import org.example.models.exceptions.PojistenecNotFoundException;
import org.example.models.exceptions.UdalostNotFoundException;
import org.example.models.services.PojistenecService;
import org.example.models.services.PojisteniService;
import org.example.models.services.PojistnaUdalostService;
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

import static org.example.models.dto.Role.*;

@Controller
@RequestMapping("/udalosti/")
public class PojistnaUdalostController {
    @Autowired
    private PojisteniService pojisteniService;

    @Autowired
    private PojistenecService pojistenecService;

    @Autowired
    private PojisteniMapper pojisteniMapper;

    @Autowired
    private PojisteniRepository pojisteniRepository;

    @Autowired
    private PojistnaUdalostRepository udalostRepository;
    @Autowired
    private PojistnaUdalostService udalostService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PojistnaUdalostMapper udalostMapper;

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(
            @PathVariable int currentPage,
            Model model
    ) {
        model.addAttribute("udalostiAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PojistnaUdalostDTO> udalosti = new ArrayList<>();
        if (user.isAdmin()) {
            udalosti = udalostService.getUdalosti(currentPage - 1);

            if (udalostRepository.count() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (udalostRepository.count() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (udalostRepository.count() > (currentPage * 10) + 10) {
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
        List<PojistenecDTO> pojistenci = new ArrayList<>();
        for (PojistnaUdalostDTO udalost : udalosti) {
            pojistenci.add(pojistenecService.getById(udalost.getPojistenecId()));
        }
        model.addAttribute("pojistenci", pojistenci);

        return "pages/udalosti/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{udalostId}/detail")
    public String renderDetail(@PathVariable long udalostId,
                               Model model
    ) {
        PojistnaUdalostDTO udalost = udalostService.getById(udalostId);
        List<PojisteniEntity> seznamPojisteni = udalostRepository.findById(udalostId).get().getPojisteni();
        model.addAttribute("udalost", udalost);
        model.addAttribute("seznamPojisteni", seznamPojisteni);
        model.addAttribute("udalostiAktivni", 1);

        return "pages/udalosti/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
    @GetMapping("novaUdalost")
    public String renderNovaUdalost(
            PojistnaUdalostDTO udalost,
            Model model
    ) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PojistenecDTO pojistenec = pojistenecService.getById(user.getPojistenec().getPojistenecId());
        List<PojisteniDTO> pojisteni = pojisteniService.getAllByPojistenecId(user.getPojistenec().getPojistenecId());

        if (udalost.getNeplatnaPojisteni() != null) {
            for (PojisteniDTO jednopojisteni : pojisteni)
                if (udalost.getNeplatnaPojisteni().contains(jednopojisteni.getPojisteniId())) {
                    jednopojisteni.setAktivni(true);
                    udalost.getPojisteniIds().remove(jednopojisteni.getPojisteniId());
                }
        }
            model.addAttribute("seznamPojisteni", pojisteni);
            model.addAttribute("pojistenec", pojistenec);
            model.addAttribute("udalostiAktivni", 1);
            return "pages/udalosti/novaUdalost";
        }

        @Secured({"ROLE_ADMIN", "ROLE_POJISTENY"})
        @PostMapping("novaUdalost")
        public String createNovaUdalost (
                @Valid @ModelAttribute PojistnaUdalostDTO udalostDTO, BindingResult result, Model model,
                RedirectAttributes redirectAttributes
    ){
            UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (result.hasErrors())
                return renderNovaUdalost(udalostDTO, model);
            List<Long> neplatnaPojisteni = udalostService.filtrujPojisteni(udalostDTO);
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
        public String renderEditForm (
        @PathVariable long pojistnaUdalostId,
        PojistnaUdalostDTO udalostDTO, Model model
    ){

            PojistnaUdalostDTO fetchedUdalost = udalostService.getById(pojistnaUdalostId);
            PojistenecDTO pojistenec = pojistenecService.getById(udalostService.getById(pojistnaUdalostId).getPojistenecId());
            model.addAttribute("pojistenec", pojistenec);
            List<PojisteniDTO> pojisteni = pojisteniService.getAllByPojistenecId(udalostService.getById(pojistnaUdalostId).getPojistenecId());
            if (udalostDTO.getNeplatnaPojisteni() != null) {
                for (PojisteniDTO jednopojisteni : pojisteni)
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
        public String editPojisteni (
        @PathVariable long pojistnaUdalostId,
        @Valid PojistnaUdalostDTO udalostDTO,
        BindingResult result,
        RedirectAttributes redirectAttributes, Model model
    ){
            if (result.hasErrors())
                return renderEditForm(pojistnaUdalostId, udalostDTO, model);
            udalostDTO.setPojistnaUdalostId(pojistnaUdalostId);
            List<Long> neplatnaPojisteni = udalostService.filtrujPojisteni(udalostDTO);
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
        public String deleteUdalost ( @PathVariable long udalostId,
        RedirectAttributes redirectAttributes){
            udalostService.remove(udalostId);
            redirectAttributes.addFlashAttribute("success", "Událost byla smazána.");

            return "redirect:/udalosti/stranka/1";
        }

        @ExceptionHandler({UdalostNotFoundException.class})
        public String handleUdalostNotFoundException (
                RedirectAttributes redirectAttributes
    ){
            redirectAttributes.addFlashAttribute("error", "Pojištěnec nenalezen.");
            return "redirect:/stranka/{currentPage}";
        }

    }


