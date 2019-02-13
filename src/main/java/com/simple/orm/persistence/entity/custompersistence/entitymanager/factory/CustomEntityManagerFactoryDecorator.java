package com.simple.orm.persistence.entity.custompersistence.entitymanager.factory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

public abstract class CustomEntityManagerFactoryDecorator implements EntityManagerFactory {

    private EntityManagerFactory entityManagerFactory;

    protected CustomEntityManagerFactoryDecorator(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        return entityManagerFactory.createEntityManager(map);
    }

    @Override
    public void close() {
        entityManagerFactory.close();
    }

    @Override
    public boolean isOpen() {
        return entityManagerFactory.isOpen();
    }
}
