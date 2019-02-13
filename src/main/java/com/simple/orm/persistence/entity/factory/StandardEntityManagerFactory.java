package com.simple.orm.persistence.entity.factory;

import com.simple.orm.persistence.DatabaseConnection;
import com.simple.orm.persistence.entity.manager.StandardEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StandardEntityManagerFactory extends DatabaseConnection implements EntityManagerFactory {


    protected List<EntityManager> entityManagerPool = new ArrayList<>();
    //todo implements cache for entity mangers

    @Override
    public EntityManager createEntityManager() {
        EntityManager entityManager = StandardEntityManager.from(super.createStatement());
        entityManagerPool.add(entityManager);
        return entityManager;
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        EntityManager entityManager = StandardEntityManager.from(super.createStatement());
        entityManagerPool.add(entityManager);
        return entityManager;
    }

    @Override
    public void close() {
        entityManagerPool.forEach(EntityManager::close);
        entityManagerPool.clear();
        super.closeDatabaseConnection();
    }

    @Override
    public boolean isOpen() {
        return super.connectionIsOpen();
    }
}
