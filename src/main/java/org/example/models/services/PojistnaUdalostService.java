package org.example.models.services;

import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;

import java.util.List;

public interface PojistnaUdalostService {

    void create(PojistnaUdalostDTO udalost, Long pojistenecId);

    List<PojistnaUdalostDTO> getUdalosti(int currentPage);

    PojistnaUdalostDTO getById(long pojistnaUdalostId);

    List<PojistnaUdalostDTO> getUdalostibyPojistenecId(int currentPage, Long pojistenecId);

    void edit(PojistnaUdalostDTO udalostDTO, Long pojistenecId);

    void remove(long udalostId);

    List<PojistnaUdalostDTO> getUdalostiByUserId(long userId);

    List<Long> filtrujPojisteni (PojistnaUdalostDTO udalost);
}
