package org.example.data.entities;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PojistenecEntity.class)
public abstract class PojistenecEntity_ {

	public static volatile SingularAttribute<PojistenecEntity, String> psc;
	public static volatile SingularAttribute<PojistenecEntity, String> telefon;
	public static volatile SingularAttribute<PojistenecEntity, String> ulice;
	public static volatile SingularAttribute<PojistenecEntity, String> prijmeni;
	public static volatile SingularAttribute<PojistenecEntity, String> mesto;
	public static volatile SingularAttribute<PojistenecEntity, Long> pojistnikId;
	public static volatile ListAttribute<PojistenecEntity, PojisteniEntity> seznamPojisteni;
	public static volatile SingularAttribute<PojistenecEntity, String> jmeno;
	public static volatile SingularAttribute<PojistenecEntity, Long> pojistenecId;
	public static volatile SingularAttribute<PojistenecEntity, UserEntity> user;
	public static volatile SingularAttribute<PojistenecEntity, String> email;

	public static final String PSC = "psc";
	public static final String TELEFON = "telefon";
	public static final String ULICE = "ulice";
	public static final String PRIJMENI = "prijmeni";
	public static final String MESTO = "mesto";
	public static final String POJISTNIK_ID = "pojistnikId";
	public static final String SEZNAM_POJISTENI = "seznamPojisteni";
	public static final String JMENO = "jmeno";
	public static final String POJISTENEC_ID = "pojistenecId";
	public static final String USER = "user";
	public static final String EMAIL = "email";

}

