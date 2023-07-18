package org.example.data.repositories;
import org.example.data.entities.InsuredEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsuredRepository extends CrudRepository<InsuredEntity, Long> {

    Page<InsuredEntity> findAll(Pageable page);

    List<InsuredEntity> findAllByPojistnikId(long pojistnikId);

}