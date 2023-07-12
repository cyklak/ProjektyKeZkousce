package org.example.models.dto.mappers;

import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PojistnaUdalostMapper {

    PojistnaUdalostEntity toEntity(PojistnaUdalostDTO source);

    @Mapping(target = "pojisteniIds", expression = "java(getPojistnaUdalostIds(source))")
    PojistnaUdalostDTO toDTO(PojistnaUdalostEntity source);

    void updatePojistnaUdalostDTO(PojistnaUdalostDTO source, @MappingTarget PojistnaUdalostDTO target);

    void updatePojistnaUdalostEntity(PojistnaUdalostDTO source, @MappingTarget PojistnaUdalostEntity target);

    default List<Long> getPojistnaUdalostIds(PojistnaUdalostEntity source) {
        List<Long> result = new ArrayList<>();
        for (PojisteniEntity pojisteni : source.getPojisteni()) {
            result.add(pojisteni.getPojisteniId());
        }
        return result;
    }

}
