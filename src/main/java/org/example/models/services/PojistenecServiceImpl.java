package org.example.models.services;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojistenecRepository;
import org.example.data.repositories.PojisteniRepository;
import org.example.data.repositories.PojistnaUdalostRepository;
import org.example.data.repositories.UserRepository;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;
import org.example.models.dto.Role;
import org.example.models.dto.mappers.PojistenecMapper;
import org.example.models.exceptions.PojistenecNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.example.models.dto.Role.POJISTENY;

@Service
public class PojistenecServiceImpl implements PojistenecService {

    @Autowired
    private PojistenecRepository pojistenecRepository;
    @Autowired
    private PojistenecMapper pojistenecMapper;

    @Autowired
    private PojisteniService pojisteniService;

    @Autowired
    private PojistnaUdalostService udalostService;

    @Autowired
    private PojistnaUdalostRepository udalostRepository;

    @Autowired
    private PojisteniRepository pojisteniRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public String create(PojistenecDTO pojistenec) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PojistenecEntity newPojistenec = pojistenecMapper.toEntity(pojistenec);
        newPojistenec.setPojistnikId(user.getUserId());
        if (pojistenec.getEmail().equals(user.getEmail())) {
            newPojistenec.setUserEntity(user);
            user.getRole().add(POJISTENY);
            pojistenecRepository.save(newPojistenec);
            userRepository.save(user);
        } else {
            String password = userService.generatePassword();
            UserEntity pojisteny = userService.createPojistenec(pojistenec, password);
            newPojistenec.setUserEntity(pojisteny);
            pojistenecRepository.save(newPojistenec);
            return password;
        }
        return "vaše současné heslo";
    }


    @Override
    public List<PojistenecDTO> getAll() {
        return StreamSupport.stream(pojistenecRepository.findAll().spliterator(), false)
                .map(i -> pojistenecMapper.toDTO(i))
                .toList();
    }

    @Override
    public List<PojistenecDTO> getPojistenci(int currentPage) {
        Page<PojistenecEntity> pageOfPeople = pojistenecRepository.findAll(PageRequest.of(currentPage, 10));
        List<PojistenecEntity> personEntities = pageOfPeople.getContent();
        List<PojistenecDTO> result = new ArrayList<>();
        for (PojistenecEntity e : personEntities) {
            result.add(pojistenecMapper.toDTO(e));
        }
        return result;
    }

    @Override
    public PojistenecDTO getById(long pojistenecId) {
        PojistenecEntity fetchedPojistenec = getPojistenecOrThrow(pojistenecId);

        return pojistenecMapper.toDTO(fetchedPojistenec);
    }

    public List<PojistenecDTO> getPojistencibyUserId(long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<PojistenecEntity> seznamPojistencu = pojistenecRepository.findAllByPojistnikId(userId);
        List<PojistenecDTO> result = new ArrayList<>();
        for (PojistenecEntity e : seznamPojistencu) {
            result.add(pojistenecMapper.toDTO(e));
        }
        return result;
    }

    @Override
    public void edit(PojistenecDTO pojistenec) {
        PojistenecEntity fetchedPojistenec = getPojistenecOrThrow(pojistenec.getPojistenecId());
        List<PojistnaUdalostEntity> udalosti = udalostRepository.findAllBypojistenecId(pojistenec.getPojistenecId());
        for (PojistnaUdalostEntity udalost : udalosti) {
            udalost.setJmenoPojisteneho(pojistenec.getJmeno());
            udalost.setPrijmeniPojisteneho(pojistenec.getPrijmeni());
            udalostRepository.save(udalost);
        }

        pojistenecMapper.updatePojistenecEntity(pojistenec, fetchedPojistenec);
        pojistenecRepository.save(fetchedPojistenec);
    }

    private PojistenecEntity getPojistenecOrThrow(long pojistenecID) {
        return pojistenecRepository
                .findById(pojistenecID)
                .orElseThrow(PojistenecNotFoundException::new);
    }

    @Override
    public void remove(long pojistenecId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity entity = pojistenecRepository.findById(pojistenecId).orElseThrow().getUserEntity();
        PojistenecEntity fetchedEntity = getPojistenecOrThrow(pojistenecId);
        List<PojistnaUdalostEntity> udalosti = udalostRepository.findAllBypojistenecId(pojistenecId);
        for (PojistnaUdalostEntity udalost: udalosti) {
            udalostRepository.delete(udalost);
        }
        List<PojisteniEntity> seznamPojisteni = pojisteniRepository.findAllBypojistenec(fetchedEntity);
        for (PojisteniEntity pojisteni: seznamPojisteni) {
            pojisteniRepository.delete(pojisteni);
        }
        pojistenecRepository.delete(fetchedEntity);
        if (user.getUserId() == entity.getUserId()) {
            user.getRole().remove(POJISTENY);
            userRepository.save(user);
        }
        if (user.getUserId() != entity.getUserId()) {
            userRepository.delete(entity);
        }

    }

}