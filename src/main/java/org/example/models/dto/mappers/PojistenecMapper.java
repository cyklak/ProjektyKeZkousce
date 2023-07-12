package org.example.models.dto.mappers;

import org.example.data.entities.PojistenecEntity;
import org.example.models.dto.PojistenecDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PojistenecMapper {
    PojistenecEntity toEntity(PojistenecDTO source);

    PojistenecDTO toDTO(PojistenecEntity source);

    void updatePojistenecDTO(PojistenecDTO source, @MappingTarget PojistenecDTO target);

    void updatePojistenecEntity(PojistenecDTO source, @MappingTarget PojistenecEntity target);
}
