package com.simple.orm.persistence.entity.manager;

import com.simple.orm.persistence.entity.custompersistence.entitymanager.CustomEntityManger;
import com.simple.orm.persistence.entity.transaction.StandardEntityTransaction;
import com.simple.orm.utils.EntityFinder;
import com.simple.orm.utils.FieldFinder;
import com.simple.orm.utils.GenericClass;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Set;


public class StandardEntityManager implements EntityManager {

    private final Statement statement;
    private EntityTransaction entityTransaction;
    private boolean isClose = false;

    private FlushModeType flushModeType = FlushModeType.AUTO;

    public static StandardEntityManager from(Statement statement) {
        validate(statement);
        return new StandardEntityManager(statement);
    }

    private static void validate(Statement statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Statement to database cannot be null");
        }
    }

    private StandardEntityManager(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void persist(Object object) {
        throwExceptionWhenEntityManagerIsClose();
        throwExceptionIfObjectClassIsNotEntity(object.getClass());
        throwExceptionWhenTransactionNotExist();
        StandardEntityManagerOperation.insert(this.statement, object);
    }


    @Override
    public <T> T merge(T object) {
        throwExceptionWhenEntityManagerIsClose();
        throwExceptionIfObjectClassIsNotEntity(object.getClass());
        throwExceptionWhenTransactionNotExist();
        return StandardEntityManagerOperation.merge(this.statement, object);
    }

    @Override
    public void remove(Object object) {
        throwExceptionWhenEntityManagerIsClose();
        throwExceptionIfObjectClassIsNotEntity(object.getClass());
        throwExceptionWhenTransactionNotExist();
        StandardEntityManagerOperation.remove(this.statement, object);
    }

    @Override
    public <T> T find(Class<T> aClass, Object id) {
        throwExceptionWhenEntityManagerIsClose();
        throwExceptionIfObjectClassIsNotEntity(aClass);
        throwExceptionIfSelectedPrimaryKeyObjectIsNotCorrectly(aClass, id);
        return StandardEntityManagerOperation.findObjectByPrimaryKeyIdentification(this.statement, aClass, id);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("getReference operation not implemented yet");
    }

    @Override
    public void flush() {
        throwExceptionWhenEntityManagerIsClose();
        throwExceptionWhenTransactionNotExist();
        throw new RuntimeException("getReference operation not implemented yet");
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        throwExceptionWhenEntityManagerIsClose();
        this.flushModeType = flushModeType;
    }

    @Override
    public FlushModeType getFlushMode() {
        throwExceptionWhenEntityManagerIsClose();
        return this.flushModeType;
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("lock operation not implemented yet");
    }

    @Override
    public void refresh(Object o) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("getReference operation not implemented yet");
    }

    @Override
    public void clear() {
        throw new RuntimeException("getReference operation not implemented yet");
    }

    @Override
    public boolean contains(Object o) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("contains operation not implemented yet");
    }

    @Override
    public Query createQuery(String s) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("createNativeQuery operation not implemented yet");
    }

    @Override
    public Query createNamedQuery(String s) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("createNativeQuery operation not implemented yet");
    }

    @Override
    public Query createNativeQuery(String s) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("createNativeQuery operation not implemented yet");
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("createNativeQuery operation not implemented yet");
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        throwExceptionWhenEntityManagerIsClose();
        throw new RuntimeException("createNativeQuery operation not implemented yet");
    }

    @Override
    public void joinTransaction() {
        throwExceptionWhenEntityManagerIsClose();
        if (this.entityTransaction == null) {
            throw new TransactionRequiredException("There is no transaction in entity manager");
        }
        if (!this.entityTransaction.isActive()) {
            this.entityTransaction.begin();
        }
    }

    @Override
    public Object getDelegate() {
        throwExceptionWhenEntityManagerIsClose();
        return new CustomEntityManger(this);
    }

    @Override
    public void close() {
        throwExceptionWhenEntityManagerIsClose();
        try {
            if (!this.statement.isClosed()) {
                this.statement.close();
                isClose = true;
            }
        } catch (SQLException e) {
            isClose = false;
        }
    }

    @Override
    public boolean isOpen() {
        return isClose;
    }

    @Override
    public EntityTransaction getTransaction() {
        if (this.entityTransaction == null) {
            this.entityTransaction = StandardEntityTransaction.from(this.statement);
        }
        return this.entityTransaction;
    }

    private void throwExceptionWhenEntityManagerIsClose() {
        if (this.isClose) {
            throw new IllegalStateException("EntityManager is container-managed or has been already closed");
        }
    }

    private void throwExceptionWhenTransactionNotExist() {
        if (this.entityTransaction == null || !this.entityTransaction.isActive()) {
            throw new TransactionRequiredException("There is no transaction in entity manager");
        }
    }

    private <T> void throwExceptionIfSelectedPrimaryKeyObjectIsNotCorrectly(Class<T> aClass, Object idKey) {
        Optional<Field> primaryKeyForSelectedEntityClass = FieldFinder.findPrimaryKeyForSelectedEntityClass(aClass);
        Field field = primaryKeyForSelectedEntityClass.orElseThrow(() -> new IllegalArgumentException("Not exist primary key"));
        if (!field.getType().equals(idKey.getClass())) {
            throw new IllegalArgumentException("Primary key object has Illegal type");
        }
    }

    private <T> void throwExceptionIfObjectClassIsNotEntity(Class<T> clazz) {
        Set<Class<?>> entities = EntityFinder.findEntities();
        if (!entities.contains(clazz)) {
            throw new IllegalArgumentException("Object is not entity");
        }
    }
}
