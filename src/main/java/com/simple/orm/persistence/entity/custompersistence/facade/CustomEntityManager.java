package com.simple.orm.persistence.entity.custompersistence.facade;


import javax.persistence.EntityTransaction;

public interface CustomEntityManager {

    void insert(Object o);

    <T> T update(T t);

    void delete(Object o);

    <T> T get(Class<T> aClass, Object o);

    void close();

    boolean isOpen();

    EntityTransaction getTransaction();

    void joinTransaction();
}
