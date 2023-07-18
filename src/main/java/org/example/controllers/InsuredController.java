package org.example.controllers;

import jakarta.validation.Valid;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojistenecRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.mappers.PojistenecMapper;
import org.example.models.dto.mappers.PojisteniMapper;
import org.example.models.exceptions.DuplicateEmailException;
import org.example.models.exceptions.PojistenecNotFoundException;
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

import static org.example.models.dto.Role.POJISTENY;
import static org.example.models.dto.Role.POJISTNIK;

@Controller
@RequestMapping("/pojistenci/")
public class InsuredController {
    private PojistenecMapper pojistenecMapper;

    private UserRepository userRepository;

    private final PojistenecServiceImpl pojistenecService = new PojistenecServiceImpl();

    private PojistenecRepository pojistenecRepository;

    private final PojisteniServiceImpl pojisteniService = new PojisteniServiceImpl();

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("stranka/{currentPage}")
    public String renderIndex(Model model, @PathVariable int currentPage) {
        model.addAttribute("pojistenciAktivni", 1);
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PojistenecDTO> pojistenci = new ArrayList<>();
        if (user.isAdmin()) {
            pojistenci = pojistenecService.getPojistenci(currentPage - 1);
        } else if ((user.getRole().contains(POJISTNIK))) {
            pojistenci = pojistenecService.getPojistencibyUserId(user.getUserId());
            model.addAttribute("paginace", 1);
        } else {
            PojistenecDTO pojisteny = pojistenecService.getById(user.getPojistenec().getPojistenecId());
            pojistenci.add(pojisteny);
        }
        model.addAttribute("pojistenci", pojistenci);
        if (user.isAdmin()) {
            if (pojistenecRepository.count() > 10) {
                model.addAttribute("soucasnaStrana", currentPage);
            }
            if (pojistenecRepository.count() > (currentPage * 10)) {
                model.addAttribute("pristiStrana", currentPage + 1);
                if (pojistenecRepository.count() > (currentPage * 10) + 10) {
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
    public String renderNovyPojistenec(@ModelAttribute PojistenecDTO pojistenec, Model model) {
        model.addAttribute("pojistenciAktivni", 1);

        return "pages/pojistenci/novyPojistenec";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @PostMapping("novyPojistenec")
    public String createNovyPojistenec(
            @Valid @ModelAttribute PojistenecDTO pojistenec,
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
                heslo = pojistenecService.create(pojistenec);
            } catch (DuplicateEmailException e) {
                result.rejectValue("email", "error", "Email je již používán.");
                return renderNovyPojistenec(pojistenec, model);
            }
        }
        else {
            heslo = pojistenecService.create(pojistenec);
        }
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl vytvořen. Heslo je: " + heslo);

        return "redirect:/pojistenci/stranka/1";
    }


    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("{pojistenecId}")
    public String renderDetail(@PathVariable Long pojistenecId,
                               Model model
    ) {
        PojistenecDTO pojistenec = pojistenecService.getById(pojistenecId);
        UserEntity pojistnik = userRepository.findById(pojistenec.getPojistnikId()).orElseThrow();
        model.addAttribute("pojistnik", pojistnik);
        model.addAttribute("pojistenec", pojistenec);
        List<PojisteniDTO> pojisteni = pojisteniService.getAllByPojistenecId(pojistenecId);
        model.addAttribute("pojisteni", pojisteni);
        model.addAttribute("pojistenciAktivni", 1);

        return "pages/pojistenci/detail";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("edit/{pojistenecId}")
    public String renderEditForm(
            @PathVariable Long pojistenecId,
            PojistenecDTO pojistenec, Model model
    ) {
        PojistenecDTO fetchedPojistenec = pojistenecService.getById(pojistenecId);
        pojistenecMapper.updatePojistenecDTO(fetchedPojistenec, pojistenec);
        model.addAttribute("pojistenciAktivni", 1);


        return "pages/pojistenci/edit";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @PostMapping("edit/{pojistenecId}")
    public String editPojistenec(
            @PathVariable long pojistenecId,
            @Valid PojistenecDTO pojistenec,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors())
            return renderEditForm(pojistenecId, pojistenec, model);

        pojistenec.setPojistenecId(pojistenecId);
        pojistenecService.edit(pojistenec);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl upraven.");

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "redirect:/pojistenci/stranka/1";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK"})
    @GetMapping("delete/{pojistenecId}")
    public String deletePojistenec(@PathVariable long pojistenecId,
                                   RedirectAttributes redirectAttributes) {
        pojistenecService.remove(pojistenecId);
        redirectAttributes.addFlashAttribute("success", "Pojištěnec byl smazán.");

        return "redirect:/pojistenci/stranka/1";
    }


    @ExceptionHandler({PojistenecNotFoundException.class})
    public String handlePojistenecNotFoundException(
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("error", "Pojištěnec nenalezen.");
        return "redirect:/pojistenci/stranka/1";
    }

}
