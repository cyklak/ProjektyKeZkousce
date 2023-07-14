package org.example.models.dto.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.data.entities.PojistenecEntity;
import org.example.models.dto.PojistenecDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class PojistenecMapperImpl implements PojistenecMapper {

    @Override
    public PojistenecEntity toEntity(PojistenecDTO source) {
        if ( source == null ) {
            return null;
        }

        PojistenecEntity pojistenecEntity = new PojistenecEntity();

        pojistenecEntity.setPojistenecId( source.getPojistenecId() );
        pojistenecEntity.setJmeno( source.getJmeno() );
        pojistenecEntity.setPrijmeni( source.getPrijmeni() );
        pojistenecEntity.setEmail( source.getEmail() );
        pojistenecEntity.setTelefon( source.getTelefon() );
        pojistenecEntity.setUlice( source.getUlice() );
        pojistenecEntity.setMesto( source.getMesto() );
        pojistenecEntity.setPsc( source.getPsc() );
        pojistenecEntity.setPojistnikId( source.getPojistnikId() );

        return pojistenecEntity;
    }

    @Override
    public PojistenecDTO toDTO(PojistenecEntity source) {
        if ( source == null ) {
            return null;
        }

        PojistenecDTO pojistenecDTO = new PojistenecDTO();

        pojistenecDTO.setJmeno( source.getJmeno() );
        pojistenecDTO.setPrijmeni( source.getPrijmeni() );
        pojistenecDTO.setEmail( source.getEmail() );
        pojistenecDTO.setTelefon( source.getTelefon() );
        pojistenecDTO.setUlice( source.getUlice() );
        pojistenecDTO.setMesto( source.getMesto() );
        pojistenecDTO.setPsc( source.getPsc() );
        pojistenecDTO.setPojistenecId( source.getPojistenecId() );
        pojistenecDTO.setPojistnikId( source.getPojistnikId() );

        return pojistenecDTO;
    }

    @Override
    public void updatePojistenecDTO(PojistenecDTO source, PojistenecDTO target) {
        if ( source == null ) {
            return;
        }

        target.setJmeno( source.getJmeno() );
        target.setPrijmeni( source.getPrijmeni() );
        target.setEmail( source.getEmail() );
        target.setTelefon( source.getTelefon() );
        target.setUlice( source.getUlice() );
        target.setMesto( source.getMesto() );
        target.setPsc( source.getPsc() );
        target.setPojistenecId( source.getPojistenecId() );
        target.setUserId( source.getUserId() );
        if ( target.getPojisteniIds() != null ) {
            List<Long> list = source.getPojisteniIds();
            if ( list != null ) {
                target.getPojisteniIds().clear();
                target.getPojisteniIds().addAll( list );
            }
            else {
                target.setPojisteniIds( null );
            }
        }
        else {
            List<Long> list = source.getPojisteniIds();
            if ( list != null ) {
                target.setPojisteniIds( new ArrayList<Long>( list ) );
            }
        }
        target.setPojistnikId( source.getPojistnikId() );
    }

    @Override
    public void updatePojistenecEntity(PojistenecDTO source, PojistenecEntity target) {
        if ( source == null ) {
            return;
        }

        target.setPojistenecId( source.getPojistenecId() );
        target.setJmeno( source.getJmeno() );
        target.setPrijmeni( source.getPrijmeni() );
        target.setEmail( source.getEmail() );
        target.setTelefon( source.getTelefon() );
        target.setUlice( source.getUlice() );
        target.setMesto( source.getMesto() );
        target.setPsc( source.getPsc() );
        target.setPojistnikId( source.getPojistnikId() );
    }
}
