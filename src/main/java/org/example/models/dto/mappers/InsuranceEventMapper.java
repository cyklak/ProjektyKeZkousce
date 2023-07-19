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

    @Mapping(target = "insuranceIds", expression = "java(getInsuranceIds(source))")
    InsuranceEventDTO toDTO(InsuranceEventEntity source);

    void updateInsuranceEventDTO(InsuranceEventDTO source, @MappingTarget InsuranceEventDTO target);

    void updateInsuranceEventEntity(InsuranceEventDTO source, @MappingTarget InsuranceEventEntity target);

    default List<Long> getInsuranceIds(InsuranceEventEntity source) {
        List<Long> result = new ArrayList<>();
        for (InsuranceEntity insurance : source.getInsurances()) {
            result.add(insurance.getInsuranceId());
        }
        return result;
    }

}
