package org.example.data.repositories;

import org.example.data.entities.InsuranceEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InsuranceEventRepository extends CrudRepository<InsuranceEventEntity, Long> {

    Page<InsuranceEventEntity> findAll(Pageable page);

    List<InsuranceEventEntity> findAllByinsuredId(Pageable page, Long insuredId);

    List<InsuranceEventEntity> findAllByinsuredId(Long insuredId);

    List<InsuranceEventEntity> findAllBypolicyholderId(Long policyholderId);

}
