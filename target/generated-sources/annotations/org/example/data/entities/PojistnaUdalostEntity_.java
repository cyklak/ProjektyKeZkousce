package org.example.data.entities;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PojistnaUdalostEntity.class)
public abstract class PojistnaUdalostEntity_ {

	public static volatile SingularAttribute<PojistnaUdalostEntity, Date> datumUdalosti;
	public static volatile SingularAttribute<PojistnaUdalostEntity, Long> pojistnaUdalostId;
	public static volatile SingularAttribute<PojistnaUdalostEntity, String> prijmeniPojisteneho;
	public static volatile SingularAttribute<PojistnaUdalostEntity, String> NazevUdalosti;
	public static volatile SingularAttribute<PojistnaUdalostEntity, String> jmenoPojisteneho;
	public static volatile SingularAttribute<PojistnaUdalostEntity, Long> pojistnikId;
	public static volatile SingularAttribute<PojistnaUdalostEntity, String> popisUdalosti;
	public static volatile SingularAttribute<PojistnaUdalostEntity, Long> pojistenecId;
	public static volatile ListAttribute<PojistnaUdalostEntity, PojisteniEntity> pojisteni;

	public static final String DATUM_UDALOSTI = "datumUdalosti";
	public static final String POJISTNA_UDALOST_ID = "pojistnaUdalostId";
	public static final String PRIJMENI_POJISTENEHO = "prijmeniPojisteneho";
	public static final String NAZEV_UDALOSTI = "NazevUdalosti";
	public static final String JMENO_POJISTENEHO = "jmenoPojisteneho";
	public static final String POJISTNIK_ID = "pojistnikId";
	public static final String POPIS_UDALOSTI = "popisUdalosti";
	public static final String POJISTENEC_ID = "pojistenecId";
	public static final String POJISTENI = "pojisteni";

}

