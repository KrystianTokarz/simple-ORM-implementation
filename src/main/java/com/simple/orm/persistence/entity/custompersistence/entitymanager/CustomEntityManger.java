package com.simple.orm.persistence.entity.custompersistence.entitymanager;

import com.simple.orm.persistence.entity.custompersistence.facade.CustomEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class CustomEntityManger implements CustomEntityManager {


    private EntityManager entityManager;

    public static CustomEntityManger from(EntityManager entityManager) {
        validate(entityManager);
        return new CustomEntityManger(entityManager);
    }

    private static void validate(EntityManager entityManager) {
        if (entityManager == null) {
            throw new IllegalArgumentException("Entity manager cannot be null");
        }
    }

    public CustomEntityManger(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void insert(Object o) {
        entityManager.getTransaction().begin();
        entityManager.persist(o);
        entityManager.getTransaction().commit();
    }

    @Override
    public <T> T update(T t) {
        entityManager.getTransaction().begin();
        T result = entityManager.merge(t);
        entityManager.getTransaction().commit();
        return result;
    }

    @Override
    public void delete(Object o) {
        entityManager.getTransaction().begin();
        entityManager.remove(o);
        entityManager.getTransaction().commit();
    }

    @Override
    public <T> T get(Class<T> aClass, Object o) {
        entityManager.getTransaction().begin();
        T result = entityManager.find(aClass, o);
        entityManager.getTransaction().commit();
        return result;
    }

    @Override
    public void close() {
        this.entityManager.close();
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return this.entityManager.getTransaction();
    }

    @Override
    public void joinTransaction() {
        this.entityManager.joinTransaction();
    }
}
