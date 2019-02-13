package com.simple.orm.persistence.entity.manager;

import com.simple.orm.persistence.entity.manager.statement.OperationExecutor;
import com.simple.orm.storage.EntityStorage;
import com.simple.orm.storage.EntityStorageImpl;
import com.simple.orm.utils.SqlOperationsPreparer;
import com.sun.istack.internal.NotNull;

import javax.persistence.EntityExistsException;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

class StandardEntityManagerOperation {

    private static EntityStorage entityStorage = EntityStorageImpl.getInstance();

    static <T> T findObjectByPrimaryKeyIdentification(Statement statement, Class<T> aClass, Object id){
        Field primaryKeyFieldForEntityClass = entityStorage.getPrimaryKeyFieldForEntityClass(aClass);
        try {
            ResultSet resultSet = OperationExecutor.executeSelectStatement(statement, primaryKeyFieldForEntityClass, id, aClass);
            return SqlOperationsPreparer.prepareSelectedObjectAfterSelectStatement(resultSet,aClass);
        } catch (Exception e) {
            throw new RuntimeException("Cannot find entity with class " + aClass.getName() + " and primary key = " + id);
        }
    }

    static void insert(Statement statement, Object object)  {
        checkCanOperateOnObject(object);
        try {
            OperationExecutor.executeInsertStatement(statement,object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot insert entity " +  object.toString(), e);
        } catch( SQLException e){
            throw new EntityExistsException("Select entity " + object.toString() + " exist in database",e);
        }
    }

    static <T> T merge(Statement statement, T object) {
        checkCanOperateOnObject(object);
        try {
            return OperationExecutor.executeMergeStatement( statement, object);
        } catch (Exception e) {
            throw new RuntimeException("Cannot merge entity " +  object.toString(), e);
        }
    }

    static  void remove(Statement statement, Object object) {
        checkCanOperateOnObject(object);
        try {
            OperationExecutor.executeRemoveStatement(statement, object);
        } catch (Exception e) {
            throw new RuntimeException("Cannot remove entity " +  object.toString(), e);
        }
    }

    private static void checkCanOperateOnObject(Object object) {
        Optional<Object> keyValue = entityStorage.getPrimaryKeyValueForObject(object);
        if(!keyValue.isPresent() && checkFieldMustBeNotNull(object)) {
            throw new IllegalArgumentException("Object primary key is not correctly set");
        }
    }

    private static boolean checkFieldMustBeNotNull(Object object){
        Field primaryKeyField = entityStorage.getPrimaryKeyFieldForEntityClass(object.getClass());
        return primaryKeyField.isAnnotationPresent(NotNull.class) || ( primaryKeyField.isAnnotationPresent(Id.class) && !primaryKeyField.isAnnotationPresent(GeneratedValue.class));
    }









}
