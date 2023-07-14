package org.example.data.repositories;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PojistnaUdalostRepository extends CrudRepository<PojistnaUdalostEntity, Long> {

    Page<PojistnaUdalostEntity> findAll(Pageable page);

    Page<PojistnaUdalostEntity> findAllBypojistenecId(Pageable page, Long pojistenecId);

    List<PojistnaUdalostEntity> findAllBypojistenecId(Long pojistenecId);

    List<PojistnaUdalostEntity> findAllBypojistnikId(Long pojistnikId);

}
