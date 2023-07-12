package org.example.models.services;

import org.example.data.entities.PojisteniEntity;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;

import java.util.List;

public interface PojisteniService {

    void create(PojisteniDTO pojisteni);

    List<PojisteniDTO> getAllByPojistenecId(Long pojistenecId);

    PojisteniDTO getById(long pojisteniId);

    void remove(long pojistenicId);

    void edit(PojisteniDTO pojisteni);

    List<PojisteniDTO> getPojisteni(int currentPage);

    PojisteniEntity getPojisteniEntity(long pojisteniID);

    List<PojisteniDTO> getPojisteniByUserId(long userId);


}
