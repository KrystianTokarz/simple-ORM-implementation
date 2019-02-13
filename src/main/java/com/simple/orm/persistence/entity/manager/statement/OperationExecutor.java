package com.simple.orm.persistence.entity.manager.statement;

import com.simple.orm.persistence.entity.operation.*;
import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import com.simple.orm.storage.EntityStorageImpl;
import com.simple.orm.utils.FieldFinder;
import com.simple.orm.utils.SqlOperationsPreparer;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OperationExecutor {

    public static <T> ResultSet executeSelectStatement(final Statement statement, final Field primaryKeyField, final Object idKey, final Class<T> clazz) throws SQLException, IllegalAccessException {
        Map<String, Object> properties = new HashMap<>();
        primaryKeyField.setAccessible(true);
        properties.put(IdentificationField.ID_FIELD_NAME.getValue(), primaryKeyField.getName());
        properties.put(IdentificationField.ID_FIELD_VALUE.getValue(), idKey);
        SqlOperation sqlOperation = new SelectOperation(clazz.getSimpleName(), properties);
        return statement.executeQuery(sqlOperation.generateSql());
    }


    public static <T> T executeMergeStatement(final Statement statement, final T object) throws SQLException, IllegalAccessException {
        Field primaryKey = EntityStorageImpl.getInstance().getPrimaryKeyFieldForEntityClass(object.getClass());
        primaryKey.setAccessible(true);
        Object keyValue = primaryKey.get(object);

        if (primaryKey.isAnnotationPresent(Id.class) && keyValue == null) {
            return executeInsertStatement(statement, object);
        } else if (primaryKey.isAnnotationPresent(Id.class) && keyValue != null) {
            return executeUpdateStatement(statement, object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static <T> T executeInsertStatement(final Statement statement, final T object) throws SQLException, IllegalAccessException {
        Field primaryKey = EntityStorageImpl.getInstance().getPrimaryKeyFieldForEntityClass(object.getClass());
        primaryKey.setAccessible(true);
        setPrimaryKeyIfNotExist(statement, primaryKey, object);
        Map<String, Object> properties = SqlOperationsPreparer.preparePropertiesFromObjectForSqlOperation(object);
        SqlOperation sqlOperation = new InsertOperation(object.getClass().getSimpleName(), properties);
        statement.execute(sqlOperation.generateSql());
        return object;
    }

    private static void setPrimaryKeyIfNotExist(Statement statement, Field primaryKey, Object object) throws IllegalAccessException, SQLException {
        Object primaryKeyObjectValue = primaryKey.get(object);
        if (primaryKeyObjectValue == null && primaryKey.isAnnotationPresent(Id.class)) {
            ResultSet resultSet = generateSelectOperationForActuallyIdKey(statement, primaryKey, object);
            setPrimaryKeyValueFromResultSet(resultSet, primaryKey, object);
        }
    }

    private static ResultSet generateSelectOperationForActuallyIdKey(Statement statement, Field primaryKey, Object object) throws SQLException {
        Map<String, Object> idPropertiesMap = new HashMap<>();
        idPropertiesMap.put(IdentificationField.ID_FIELD_NAME.getValue(), primaryKey.getName());
        SelectLastIdValueOperation selectLastIdValueOperation = new SelectLastIdValueOperation(object.getClass().getSimpleName(), idPropertiesMap);
        return statement.executeQuery(selectLastIdValueOperation.generateSql());
    }

    private static void setPrimaryKeyValueFromResultSet(ResultSet resultSet, Field primaryKey, Object object) throws SQLException, IllegalAccessException {
        if (resultSet.next()) {
            Long primaryKeyValue = resultSet.getLong(1);
            primaryKey.set(object, ++primaryKeyValue);
        }
    }

    public static <T> T executeUpdateStatement(final Statement statement, T object) throws IllegalAccessException, SQLException {
        Map<String, Object> properties = SqlOperationsPreparer.preparePropertiesFromObjectForSqlOperation(object);
        Optional<Field> primaryKeyForSelectedEntityClass = FieldFinder.findPrimaryKeyForSelectedEntityClass(object.getClass());
        primaryKeyForSelectedEntityClass.ifPresent(field -> properties.putAll(SqlOperationsPreparer.createPropertiesWithPrimaryKeyValues(properties, field)));
        SqlOperation sqlOperation = new UpdateOperation(object.getClass().getSimpleName(), properties);
        statement.addBatch(sqlOperation.generateSql());
        return object;
    }


    public static void executeRemoveStatement(final Statement statement, Object object) throws IllegalAccessException, SQLException {
        Map<String, Object> properties = SqlOperationsPreparer.preparePropertiesFromObjectForSqlOperation(object);
        Optional<Field> primaryKeyForSelectedEntityClass = FieldFinder.findPrimaryKeyForSelectedEntityClass(object.getClass());
        primaryKeyForSelectedEntityClass.ifPresent(field -> properties.putAll(SqlOperationsPreparer.createPropertiesWithPrimaryKeyValues(properties, field)));
        SqlOperation sqlOperation = new DeleteOperation(object.getClass().getSimpleName(), properties);
        statement.addBatch(sqlOperation.generateSql());
    }


}
