package org.example.models.dto.mappers;

import org.example.data.entities.PojisteniEntity;
import org.example.models.dto.PojisteniDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PojisteniMapper {

    PojisteniEntity toEntity(PojisteniDTO source);

    PojisteniDTO toDTO(PojisteniEntity source);


    void updatePojisteniDTO(PojisteniDTO source, @MappingTarget PojisteniDTO target);

    void updatePojisteniEntity(PojisteniDTO source, @MappingTarget PojisteniEntity target);
}
