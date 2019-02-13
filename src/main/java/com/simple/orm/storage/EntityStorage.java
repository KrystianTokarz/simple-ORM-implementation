package com.simple.orm.storage;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EntityStorage {

    Set<Class<?>> getEntitiesClass();

    Field getPrimaryKeyFieldForEntityClass(Class<?> clazz);

    Set<Field> getAllFieldForEntityClass(Class<?> clazz);

    Optional<Object> getPrimaryKeyValueForObject(Object object);
}
