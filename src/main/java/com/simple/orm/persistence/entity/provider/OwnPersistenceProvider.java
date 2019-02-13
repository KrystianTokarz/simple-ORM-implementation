package com.simple.orm.persistence.entity.provider;

import com.simple.orm.persistence.entity.factory.StandardEntityManagerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.Map;

public class OwnPersistenceProvider implements PersistenceProvider {


    @Override
    public EntityManagerFactory createEntityManagerFactory(String s, Map map) {
        return new StandardEntityManagerFactory();
    }

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map map) {
        return new StandardEntityManagerFactory();
    }
}
