package com.simple.orm;

import com.simple.orm.persistence.database.DatabaseManagerService;
import com.simple.orm.persistence.entity.PersistenceEntityManagerFactory;
import com.simple.orm.utils.EntityFinder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;
import java.util.Set;

public class Invoker {

    public static void main(String[] args) throws SQLException {
        Set<Class<?>> entities = EntityFinder.findEntities();
        DatabaseManagerService.getQueryManagerInstance().createTables(entities);
        testEntityManager();
    }

    private static void testEntityManager() {
        EntityManagerFactory entityManagerFactory = PersistenceEntityManagerFactory.createEntityManagerFactory();
        EntityManager entityManagerInstance = entityManagerFactory.createEntityManager();
        System.out.println(entityManagerInstance);
    }
}
