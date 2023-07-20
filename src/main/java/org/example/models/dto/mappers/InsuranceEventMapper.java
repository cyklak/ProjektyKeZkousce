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

    /** converts an InsuranceEventDTO into an InsuranceEventEntity
     * @param source
     * @return
     */
    InsuranceEventEntity toEntity(InsuranceEventDTO source);

    /** converts an InsuranceEventEntity into an InsuranceEventDTO
     * @param source
     * @return
     */
    @Mapping(target = "insuranceIds", expression = "java(getInsuranceIds(source))")
    InsuranceEventDTO toDTO(InsuranceEventEntity source);

    /** updates an InsuranceEventDTO
     * @param source
     * @param target
     */
    void updateInsuranceEventDTO(InsuranceEventDTO source, @MappingTarget InsuranceEventDTO target);

    /** updates an InsuranceEventEntity
     * @param source
     * @param target
     */
    void updateInsuranceEventEntity(InsuranceEventDTO source, @MappingTarget InsuranceEventEntity target);

    /**
     * @param source
     * @return sets insuranceIds parameter of InsuranceEventDTO
     */
    default List<Long> getInsuranceIds(InsuranceEventEntity source) {
        List<Long> result = new ArrayList<>();
        for (InsuranceEntity insurance : source.getInsurances()) {
            result.add(insurance.getInsuranceId());
        }
        return result;
    }

}
