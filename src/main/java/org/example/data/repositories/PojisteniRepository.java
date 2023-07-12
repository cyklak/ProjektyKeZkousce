package org.example.data.repositories;

import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PojisteniRepository extends CrudRepository<PojisteniEntity, Long> {

    Page<PojisteniEntity> findAll(Pageable page);

    List<PojisteniEntity> findAllBypojistenec (PojistenecEntity pojistenec);
}
