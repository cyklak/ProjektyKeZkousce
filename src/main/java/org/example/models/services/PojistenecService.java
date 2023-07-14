package org.example.models.services;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.UserEntity;
import org.example.models.dto.PojistenecDTO;

import java.util.List;

public interface PojistenecService {
    String create(PojistenecDTO pojistenec);

    List<PojistenecDTO> getAll();

    PojistenecDTO getById(long pojistenecId);

    void edit(PojistenecDTO pojistenec);

    void remove(long pojistenecId);

    List<PojistenecDTO> getPojistenci(int currentPage);

    List<PojistenecDTO> getPojistencibyUserId (long userId);
}
