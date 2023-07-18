package org.example.models.dto.mappers;

import org.example.data.entities.InsuranceEntity;
import org.example.data.entities.InsuranceEventEntity;
import org.example.models.dto.InsuranceEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InsuranceEventMapper {

    InsuranceEventEntity toEntity(InsuranceEventDTO source);

    @Mapping(target = "pojisteniIds", expression = "java(getPojistnaUdalostIds(source))")
    InsuranceEventDTO toDTO(InsuranceEventEntity source);

    void updatePojistnaUdalostDTO(InsuranceEventDTO source, @MappingTarget InsuranceEventDTO target);

    void updatePojistnaUdalostEntity(InsuranceEventDTO source, @MappingTarget InsuranceEventEntity target);

    default List<Long> getPojistnaUdalostIds(InsuranceEventEntity source) {
        List<Long> result = new ArrayList<>();
        for (InsuranceEntity pojisteni : source.getPojisteni()) {
            result.add(pojisteni.getPojisteniId());
        }
        return result;
    }

}
