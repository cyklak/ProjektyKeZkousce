package org.example.data.entities;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;
import org.example.models.dto.Role;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserEntity.class)
public abstract class UserEntity_ {

	public static volatile SingularAttribute<UserEntity, String> password;
	public static volatile ListAttribute<UserEntity, Role> role;
	public static volatile SingularAttribute<UserEntity, Boolean> admin;
	public static volatile SingularAttribute<UserEntity, Long> userId;
	public static volatile SingularAttribute<UserEntity, String> email;
	public static volatile SingularAttribute<UserEntity, PojistenecEntity> pojistenec;

	public static final String PASSWORD = "password";
	public static final String ROLE = "role";
	public static final String ADMIN = "admin";
	public static final String USER_ID = "userId";
	public static final String EMAIL = "email";
	public static final String POJISTENEC = "pojistenec";

}

