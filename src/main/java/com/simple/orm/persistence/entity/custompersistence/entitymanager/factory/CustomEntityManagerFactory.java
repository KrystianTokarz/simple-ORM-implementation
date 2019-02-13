package com.simple.orm.persistence.entity.custompersistence.entitymanager.factory;

import com.simple.orm.persistence.entity.custompersistence.entitymanager.CustomEntityManger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Map;

public class CustomEntityManagerFactory extends CustomEntityManagerFactoryDecorator {


    public CustomEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public CustomEntityManger createCustomEntityManger() {
        EntityManager entityManager = super.createEntityManager();
        return CustomEntityManger.from(entityManager);
    }

    public CustomEntityManger createCustomEntityManger(Map map) {
        EntityManager entityManager = super.createEntityManager();
        return CustomEntityManger.from(entityManager);
    }

    public void close() {
        super.close();
    }

    public boolean isOpen() {
        return super.isOpen();
    }
}
