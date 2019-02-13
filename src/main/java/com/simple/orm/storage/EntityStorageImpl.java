package com.simple.orm.storage;

import com.simple.orm.utils.EntityFinder;
import com.simple.orm.utils.FieldFinder;

import java.lang.reflect.Field;
import java.util.*;

public class EntityStorageImpl implements EntityStorage {

    private Set<Class<?>> entitiesClass = new HashSet<>();
    private Map<Class, Field> entitiesClassWithPrimaryKeyField = new HashMap<>();
    private Map<Class, Set<Field>> entitiesClassWithFields = new HashMap<>();

    private static EntityStorageImpl entityStorage;

    public static EntityStorage getInstance() {
        if (entityStorage == null) {
            entityStorage = new EntityStorageImpl();
            entityStorage.initialize();
            return entityStorage;
        } else {
            return entityStorage;
        }
    }

    private void initialize() {
        this.entitiesClass = EntityFinder.findEntities();
        this.entitiesClassWithPrimaryKeyField = FieldFinder.findPrimaryKeyFieldForEntities(this.entitiesClass);
        this.entitiesClassWithFields = FieldFinder.findAllColumnNamesForEntities(this.entitiesClass);
    }

    @Override
    public Set<Class<?>> getEntitiesClass() {
        return entitiesClass;
    }

    @Override
    public Field getPrimaryKeyFieldForEntityClass(Class<?> clazz) {
        return entitiesClassWithPrimaryKeyField.get(clazz);
    }

    @Override
    public Set<Field> getAllFieldForEntityClass(Class<?> clazz) {
        return entitiesClassWithFields.get(clazz);
    }

    public Optional<Object> getPrimaryKeyValueForObject(Object object){
        Field primaryKey =  entitiesClassWithPrimaryKeyField.get(object.getClass());
        primaryKey.setAccessible(true);
        try {
            return Optional.ofNullable(primaryKey.get(object));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }


}
