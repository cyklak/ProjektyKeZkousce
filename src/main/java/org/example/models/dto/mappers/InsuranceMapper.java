package org.example.models.dto.mappers;

import org.example.data.entities.InsuranceEntity;
import org.example.models.dto.InsuranceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    /** converts an InsuranceDTO into an InsuranceEntity
     * @param source
     * @return
     */
    InsuranceEntity toEntity(InsuranceDTO source);

    /** converts an InsuranceEntity into an InsuranceEventDTO
     * @param source
     * @return
     */
    InsuranceDTO toDTO(InsuranceEntity source);


    /** updates an InsuranceEventDTO
     * @param source
     * @param target
     */
    void updateInsuranceDTO(InsuranceDTO source, @MappingTarget InsuranceDTO target);

    /** updates an InsuranceEventEntity
     * @param source
     * @param target
     */
    void updateInsuranceEntity(InsuranceDTO source, @MappingTarget InsuranceEntity target);
}
