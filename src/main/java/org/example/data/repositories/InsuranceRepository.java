package org.example.data.repositories;

import org.example.data.entities.InsuredEntity;
import org.example.data.entities.InsuranceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsuranceRepository extends CrudRepository<InsuranceEntity, Long> {

    Page<InsuranceEntity> findAll(Pageable page);

    List<InsuranceEntity> findAllBypojistenec (InsuredEntity pojistenec);
}
