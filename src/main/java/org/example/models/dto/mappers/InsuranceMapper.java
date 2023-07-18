package org.example.models.dto.mappers;

import org.example.data.entities.InsuranceEntity;
import org.example.models.dto.InsuranceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    InsuranceEntity toEntity(InsuranceDTO source);

    InsuranceDTO toDTO(InsuranceEntity source);


    void updatePojisteniDTO(InsuranceDTO source, @MappingTarget InsuranceDTO target);

    void updatePojisteniEntity(InsuranceDTO source, @MappingTarget InsuranceEntity target);
}
