package org.example.data.repositories;
import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PojistenecRepository extends CrudRepository<PojistenecEntity, Long> {

    Page<PojistenecEntity> findAll(Pageable page);

    List<PojistenecEntity> findAllByPojistnikId(long pojistnikId);

}