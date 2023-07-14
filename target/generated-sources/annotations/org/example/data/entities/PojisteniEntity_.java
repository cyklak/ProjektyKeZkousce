package org.example.data.entities;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PojisteniEntity.class)
public abstract class PojisteniEntity_ {

	public static volatile SingularAttribute<PojisteniEntity, LocalDate> platnostDo;
	public static volatile ListAttribute<PojisteniEntity, PojistnaUdalostEntity> seznamUdalosti;
	public static volatile SingularAttribute<PojisteniEntity, Long> pojisteniId;
	public static volatile SingularAttribute<PojisteniEntity, String> castka;
	public static volatile SingularAttribute<PojisteniEntity, String> predmetPojisteni;
	public static volatile SingularAttribute<PojisteniEntity, LocalDate> platnostOd;
	public static volatile SingularAttribute<PojisteniEntity, String> typPojisteni;
	public static volatile SingularAttribute<PojisteniEntity, PojistenecEntity> pojistenec;

	public static final String PLATNOST_DO = "platnostDo";
	public static final String SEZNAM_UDALOSTI = "seznamUdalosti";
	public static final String POJISTENI_ID = "pojisteniId";
	public static final String CASTKA = "castka";
	public static final String PREDMET_POJISTENI = "predmetPojisteni";
	public static final String PLATNOST_OD = "platnostOd";
	public static final String TYP_POJISTENI = "typPojisteni";
	public static final String POJISTENEC = "pojistenec";

}

