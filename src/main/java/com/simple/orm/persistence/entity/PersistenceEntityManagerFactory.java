package com.simple.orm.persistence.entity;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class PersistenceEntityManagerFactory {

    private static final String PERSISTENCE_UNIT_NAME = "ownpersistenceprovider";

    private PersistenceEntityManagerFactory() {
    }

    public static EntityManagerFactory createEntityManagerFactory(){
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }


}
