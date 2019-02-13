package com.simple.orm.utils;

import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SqlOperationsPreparer {


    public static Map<String, Object> preparePropertiesFromObjectForSqlOperation(Object object) throws IllegalAccessException {
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        Map<String, Object> properties = new HashMap<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object fieldValue = declaredField.get(object);
            if (declaredField.isAnnotationPresent(NotNull.class) && fieldValue == null) {
                throw new IllegalArgumentException();
            } else {
                properties.put(declaredField.getName(), fieldValue);
            }
        }
        return properties;
    }

    public static Map<String, Object> createPropertiesWithPrimaryKeyValues(Map<String, Object> oryginalProperties, Field iFiled) {
        Map<String, Object> properties = new HashMap<>();
        Object fieldObject = oryginalProperties.get(iFiled.getName());
        properties.put(IdentificationField.ID_FIELD_VALUE.getValue(), fieldObject);
        properties.put(IdentificationField.ID_FIELD_NAME.getValue(), iFiled.getName());
        return properties;
    }


    public static <T> T prepareSelectedObjectAfterSelectStatement(ResultSet resultSet, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException {
        T newInstance = null;
        boolean next = resultSet.next();
        if (next) {
            newInstance = clazz.newInstance();
            Field[] declaredFields = newInstance.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                int column = resultSet.findColumn(declaredField.getName());
                declaredField.set(newInstance, resultSet.getObject(column));
            }
        }

        return newInstance;
    }
}
