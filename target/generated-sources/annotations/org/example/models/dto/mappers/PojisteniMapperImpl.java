package org.example.models.dto.mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.data.entities.PojistenecEntity;
import org.example.data.entities.PojisteniEntity;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.models.dto.PojistenecDTO;
import org.example.models.dto.PojisteniDTO;
import org.example.models.dto.PojistnaUdalostDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class PojisteniMapperImpl implements PojisteniMapper {

    @Override
    public PojisteniEntity toEntity(PojisteniDTO source) {
        if ( source == null ) {
            return null;
        }

        PojisteniEntity pojisteniEntity = new PojisteniEntity();

        pojisteniEntity.setTypPojisteni( source.getTypPojisteni() );
        pojisteniEntity.setPojisteniId( source.getPojisteniId() );
        pojisteniEntity.setCastka( source.getCastka() );
        pojisteniEntity.setPredmetPojisteni( source.getPredmetPojisteni() );
        pojisteniEntity.setPlatnostOd( source.getPlatnostOd() );
        pojisteniEntity.setPlatnostDo( source.getPlatnostDo() );
        pojisteniEntity.setPojistenec( pojistenecDTOToPojistenecEntity( source.getPojistenec() ) );
        pojisteniEntity.setSeznamUdalosti( pojistnaUdalostDTOListToPojistnaUdalostEntityList( source.getSeznamUdalosti() ) );

        return pojisteniEntity;
    }

    @Override
    public PojisteniDTO toDTO(PojisteniEntity source) {
        if ( source == null ) {
            return null;
        }

        PojisteniDTO pojisteniDTO = new PojisteniDTO();

        pojisteniDTO.setPojisteniId( source.getPojisteniId() );
        pojisteniDTO.setTypPojisteni( source.getTypPojisteni() );
        pojisteniDTO.setCastka( source.getCastka() );
        pojisteniDTO.setPredmetPojisteni( source.getPredmetPojisteni() );
        pojisteniDTO.setPlatnostOd( source.getPlatnostOd() );
        pojisteniDTO.setPlatnostDo( source.getPlatnostDo() );
        pojisteniDTO.setPojistenec( pojistenecEntityToPojistenecDTO( source.getPojistenec() ) );
        pojisteniDTO.setSeznamUdalosti( pojistnaUdalostEntityListToPojistnaUdalostDTOList( source.getSeznamUdalosti() ) );

        return pojisteniDTO;
    }

    @Override
    public void updatePojisteniDTO(PojisteniDTO source, PojisteniDTO target) {
        if ( source == null ) {
            return;
        }

        target.setPojisteniId( source.getPojisteniId() );
        target.setTypPojisteni( source.getTypPojisteni() );
        target.setCastka( source.getCastka() );
        target.setPredmetPojisteni( source.getPredmetPojisteni() );
        target.setPlatnostOd( source.getPlatnostOd() );
        target.setPlatnostDo( source.getPlatnostDo() );
        target.setPojistenec( source.getPojistenec() );
        if ( target.getSeznamUdalosti() != null ) {
            List<PojistnaUdalostDTO> list = source.getSeznamUdalosti();
            if ( list != null ) {
                target.getSeznamUdalosti().clear();
                target.getSeznamUdalosti().addAll( list );
            }
            else {
                target.setSeznamUdalosti( null );
            }
        }
        else {
            List<PojistnaUdalostDTO> list = source.getSeznamUdalosti();
            if ( list != null ) {
                target.setSeznamUdalosti( new ArrayList<PojistnaUdalostDTO>( list ) );
            }
        }
        target.setAktivni( source.isAktivni() );
    }

    @Override
    public void updatePojisteniEntity(PojisteniDTO source, PojisteniEntity target) {
        if ( source == null ) {
            return;
        }

        target.setTypPojisteni( source.getTypPojisteni() );
        target.setPojisteniId( source.getPojisteniId() );
        target.setCastka( source.getCastka() );
        target.setPredmetPojisteni( source.getPredmetPojisteni() );
        target.setPlatnostOd( source.getPlatnostOd() );
        target.setPlatnostDo( source.getPlatnostDo() );
        if ( source.getPojistenec() != null ) {
            if ( target.getPojistenec() == null ) {
                target.setPojistenec( new PojistenecEntity() );
            }
            pojistenecDTOToPojistenecEntity1( source.getPojistenec(), target.getPojistenec() );
        }
        else {
            target.setPojistenec( null );
        }
        if ( target.getSeznamUdalosti() != null ) {
            List<PojistnaUdalostEntity> list = pojistnaUdalostDTOListToPojistnaUdalostEntityList( source.getSeznamUdalosti() );
            if ( list != null ) {
                target.getSeznamUdalosti().clear();
                target.getSeznamUdalosti().addAll( list );
            }
            else {
                target.setSeznamUdalosti( null );
            }
        }
        else {
            List<PojistnaUdalostEntity> list = pojistnaUdalostDTOListToPojistnaUdalostEntityList( source.getSeznamUdalosti() );
            if ( list != null ) {
                target.setSeznamUdalosti( list );
            }
        }
    }

    protected PojistenecEntity pojistenecDTOToPojistenecEntity(PojistenecDTO pojistenecDTO) {
        if ( pojistenecDTO == null ) {
            return null;
        }

        PojistenecEntity pojistenecEntity = new PojistenecEntity();

        pojistenecEntity.setPojistenecId( pojistenecDTO.getPojistenecId() );
        pojistenecEntity.setJmeno( pojistenecDTO.getJmeno() );
        pojistenecEntity.setPrijmeni( pojistenecDTO.getPrijmeni() );
        pojistenecEntity.setEmail( pojistenecDTO.getEmail() );
        pojistenecEntity.setTelefon( pojistenecDTO.getTelefon() );
        pojistenecEntity.setUlice( pojistenecDTO.getUlice() );
        pojistenecEntity.setMesto( pojistenecDTO.getMesto() );
        pojistenecEntity.setPsc( pojistenecDTO.getPsc() );
        pojistenecEntity.setPojistnikId( pojistenecDTO.getPojistnikId() );

        return pojistenecEntity;
    }

    protected PojistnaUdalostEntity pojistnaUdalostDTOToPojistnaUdalostEntity(PojistnaUdalostDTO pojistnaUdalostDTO) {
        if ( pojistnaUdalostDTO == null ) {
            return null;
        }

        PojistnaUdalostEntity pojistnaUdalostEntity = new PojistnaUdalostEntity();

        pojistnaUdalostEntity.setPojistnaUdalostId( pojistnaUdalostDTO.getPojistnaUdalostId() );
        pojistnaUdalostEntity.setNazevUdalosti( pojistnaUdalostDTO.getNazevUdalosti() );
        if ( pojistnaUdalostDTO.getDatumUdalosti() != null ) {
            pojistnaUdalostEntity.setDatumUdalosti( Date.from( pojistnaUdalostDTO.getDatumUdalosti().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }
        pojistnaUdalostEntity.setPopisUdalosti( pojistnaUdalostDTO.getPopisUdalosti() );
        pojistnaUdalostEntity.setPojistenecId( pojistnaUdalostDTO.getPojistenecId() );
        pojistnaUdalostEntity.setPojistnikId( pojistnaUdalostDTO.getPojistnikId() );
        pojistnaUdalostEntity.setJmenoPojisteneho( pojistnaUdalostDTO.getJmenoPojisteneho() );
        pojistnaUdalostEntity.setPrijmeniPojisteneho( pojistnaUdalostDTO.getPrijmeniPojisteneho() );

        return pojistnaUdalostEntity;
    }

    protected List<PojistnaUdalostEntity> pojistnaUdalostDTOListToPojistnaUdalostEntityList(List<PojistnaUdalostDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<PojistnaUdalostEntity> list1 = new ArrayList<PojistnaUdalostEntity>( list.size() );
        for ( PojistnaUdalostDTO pojistnaUdalostDTO : list ) {
            list1.add( pojistnaUdalostDTOToPojistnaUdalostEntity( pojistnaUdalostDTO ) );
        }

        return list1;
    }

    protected PojistenecDTO pojistenecEntityToPojistenecDTO(PojistenecEntity pojistenecEntity) {
        if ( pojistenecEntity == null ) {
            return null;
        }

        PojistenecDTO pojistenecDTO = new PojistenecDTO();

        pojistenecDTO.setJmeno( pojistenecEntity.getJmeno() );
        pojistenecDTO.setPrijmeni( pojistenecEntity.getPrijmeni() );
        pojistenecDTO.setEmail( pojistenecEntity.getEmail() );
        pojistenecDTO.setTelefon( pojistenecEntity.getTelefon() );
        pojistenecDTO.setUlice( pojistenecEntity.getUlice() );
        pojistenecDTO.setMesto( pojistenecEntity.getMesto() );
        pojistenecDTO.setPsc( pojistenecEntity.getPsc() );
        pojistenecDTO.setPojistenecId( pojistenecEntity.getPojistenecId() );
        pojistenecDTO.setPojistnikId( pojistenecEntity.getPojistnikId() );

        return pojistenecDTO;
    }

    protected PojistnaUdalostDTO pojistnaUdalostEntityToPojistnaUdalostDTO(PojistnaUdalostEntity pojistnaUdalostEntity) {
        if ( pojistnaUdalostEntity == null ) {
            return null;
        }

        PojistnaUdalostDTO pojistnaUdalostDTO = new PojistnaUdalostDTO();

        pojistnaUdalostDTO.setPojistnaUdalostId( pojistnaUdalostEntity.getPojistnaUdalostId() );
        pojistnaUdalostDTO.setNazevUdalosti( pojistnaUdalostEntity.getNazevUdalosti() );
        if ( pojistnaUdalostEntity.getDatumUdalosti() != null ) {
            pojistnaUdalostDTO.setDatumUdalosti( LocalDateTime.ofInstant( pojistnaUdalostEntity.getDatumUdalosti().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        pojistnaUdalostDTO.setPopisUdalosti( pojistnaUdalostEntity.getPopisUdalosti() );
        pojistnaUdalostDTO.setPojistenecId( pojistnaUdalostEntity.getPojistenecId() );
        pojistnaUdalostDTO.setPojistnikId( pojistnaUdalostEntity.getPojistnikId() );
        pojistnaUdalostDTO.setJmenoPojisteneho( pojistnaUdalostEntity.getJmenoPojisteneho() );
        pojistnaUdalostDTO.setPrijmeniPojisteneho( pojistnaUdalostEntity.getPrijmeniPojisteneho() );

        return pojistnaUdalostDTO;
    }

    protected List<PojistnaUdalostDTO> pojistnaUdalostEntityListToPojistnaUdalostDTOList(List<PojistnaUdalostEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<PojistnaUdalostDTO> list1 = new ArrayList<PojistnaUdalostDTO>( list.size() );
        for ( PojistnaUdalostEntity pojistnaUdalostEntity : list ) {
            list1.add( pojistnaUdalostEntityToPojistnaUdalostDTO( pojistnaUdalostEntity ) );
        }

        return list1;
    }

    protected void pojistenecDTOToPojistenecEntity1(PojistenecDTO pojistenecDTO, PojistenecEntity mappingTarget) {
        if ( pojistenecDTO == null ) {
            return;
        }

        mappingTarget.setPojistenecId( pojistenecDTO.getPojistenecId() );
        mappingTarget.setJmeno( pojistenecDTO.getJmeno() );
        mappingTarget.setPrijmeni( pojistenecDTO.getPrijmeni() );
        mappingTarget.setEmail( pojistenecDTO.getEmail() );
        mappingTarget.setTelefon( pojistenecDTO.getTelefon() );
        mappingTarget.setUlice( pojistenecDTO.getUlice() );
        mappingTarget.setMesto( pojistenecDTO.getMesto() );
        mappingTarget.setPsc( pojistenecDTO.getPsc() );
        mappingTarget.setPojistnikId( pojistenecDTO.getPojistnikId() );
    }
}
