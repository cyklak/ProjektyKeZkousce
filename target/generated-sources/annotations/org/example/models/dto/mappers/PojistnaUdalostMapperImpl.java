package org.example.models.dto.mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.data.entities.PojistnaUdalostEntity;
import org.example.models.dto.PojistnaUdalostDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.6 (Eclipse Adoptium)"
)
@Component
public class PojistnaUdalostMapperImpl implements PojistnaUdalostMapper {

    @Override
    public PojistnaUdalostEntity toEntity(PojistnaUdalostDTO source) {
        if ( source == null ) {
            return null;
        }

        PojistnaUdalostEntity pojistnaUdalostEntity = new PojistnaUdalostEntity();

        pojistnaUdalostEntity.setPojistnaUdalostId( source.getPojistnaUdalostId() );
        pojistnaUdalostEntity.setNazevUdalosti( source.getNazevUdalosti() );
        if ( source.getDatumUdalosti() != null ) {
            pojistnaUdalostEntity.setDatumUdalosti( Date.from( source.getDatumUdalosti().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }
        pojistnaUdalostEntity.setPopisUdalosti( source.getPopisUdalosti() );
        pojistnaUdalostEntity.setPojistenecId( source.getPojistenecId() );
        pojistnaUdalostEntity.setPojistnikId( source.getPojistnikId() );
        pojistnaUdalostEntity.setJmenoPojisteneho( source.getJmenoPojisteneho() );
        pojistnaUdalostEntity.setPrijmeniPojisteneho( source.getPrijmeniPojisteneho() );

        return pojistnaUdalostEntity;
    }

    @Override
    public PojistnaUdalostDTO toDTO(PojistnaUdalostEntity source) {
        if ( source == null ) {
            return null;
        }

        PojistnaUdalostDTO pojistnaUdalostDTO = new PojistnaUdalostDTO();

        pojistnaUdalostDTO.setPojistnaUdalostId( source.getPojistnaUdalostId() );
        pojistnaUdalostDTO.setNazevUdalosti( source.getNazevUdalosti() );
        if ( source.getDatumUdalosti() != null ) {
            pojistnaUdalostDTO.setDatumUdalosti( LocalDateTime.ofInstant( source.getDatumUdalosti().toInstant(), ZoneOffset.UTC ).toLocalDate() );
        }
        pojistnaUdalostDTO.setPopisUdalosti( source.getPopisUdalosti() );
        pojistnaUdalostDTO.setPojistenecId( source.getPojistenecId() );
        pojistnaUdalostDTO.setPojistnikId( source.getPojistnikId() );
        pojistnaUdalostDTO.setJmenoPojisteneho( source.getJmenoPojisteneho() );
        pojistnaUdalostDTO.setPrijmeniPojisteneho( source.getPrijmeniPojisteneho() );

        pojistnaUdalostDTO.setPojisteniIds( getPojistnaUdalostIds(source) );

        return pojistnaUdalostDTO;
    }

    @Override
    public void updatePojistnaUdalostDTO(PojistnaUdalostDTO source, PojistnaUdalostDTO target) {
        if ( source == null ) {
            return;
        }

        target.setPojistnaUdalostId( source.getPojistnaUdalostId() );
        target.setNazevUdalosti( source.getNazevUdalosti() );
        target.setDatumUdalosti( source.getDatumUdalosti() );
        target.setPopisUdalosti( source.getPopisUdalosti() );
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
        target.setPojistenecId( source.getPojistenecId() );
        target.setPojistnikId( source.getPojistnikId() );
        target.setJmenoPojisteneho( source.getJmenoPojisteneho() );
        target.setPrijmeniPojisteneho( source.getPrijmeniPojisteneho() );
        if ( target.getNeplatnaPojisteni() != null ) {
            List<Long> list1 = source.getNeplatnaPojisteni();
            if ( list1 != null ) {
                target.getNeplatnaPojisteni().clear();
                target.getNeplatnaPojisteni().addAll( list1 );
            }
            else {
                target.setNeplatnaPojisteni( null );
            }
        }
        else {
            List<Long> list1 = source.getNeplatnaPojisteni();
            if ( list1 != null ) {
                target.setNeplatnaPojisteni( new ArrayList<Long>( list1 ) );
            }
        }
    }

    @Override
    public void updatePojistnaUdalostEntity(PojistnaUdalostDTO source, PojistnaUdalostEntity target) {
        if ( source == null ) {
            return;
        }

        target.setPojistnaUdalostId( source.getPojistnaUdalostId() );
        target.setNazevUdalosti( source.getNazevUdalosti() );
        if ( source.getDatumUdalosti() != null ) {
            target.setDatumUdalosti( Date.from( source.getDatumUdalosti().atStartOfDay( ZoneOffset.UTC ).toInstant() ) );
        }
        else {
            target.setDatumUdalosti( null );
        }
        target.setPopisUdalosti( source.getPopisUdalosti() );
        target.setPojistenecId( source.getPojistenecId() );
        target.setPojistnikId( source.getPojistnikId() );
        target.setJmenoPojisteneho( source.getJmenoPojisteneho() );
        target.setPrijmeniPojisteneho( source.getPrijmeniPojisteneho() );
    }
}
