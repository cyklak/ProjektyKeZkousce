package org.example.models.dto.mappers;

import org.example.data.entities.InsuredEntity;
import org.example.models.dto.InsuredDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InsuredMapper {
    /** converts an InsuredDTO to an InsuredEntity
     * @param source
     * @return
     */
    InsuredEntity toEntity(InsuredDTO source);

    /** converts an InsuredEntity to an InsuredDTO
     * @param source
     * @return
     */
    InsuredDTO toDTO(InsuredEntity source);

    /** updates an InsuredDTO
     * @param source
     * @param target
     */
    void updateInsuredDTO(InsuredDTO source, @MappingTarget InsuredDTO target);

    /** updates an InsuredEntity
     * @param source
     * @param target
     */
    void updateInsuredEntity(InsuredDTO source, @MappingTarget InsuredEntity target);
}
