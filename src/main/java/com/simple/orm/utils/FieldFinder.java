package com.simple.orm.utils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class FieldFinder {


    public static  Map<Class, Field> findPrimaryKeyFieldForEntities(Set<Class<?>> entityClasses){
        Map<Class, Field> result = new HashMap<>();
        for (Class entityClass : entityClasses) {
            if(entityClass.isAnnotationPresent(Entity.class)){
                Optional<Field> annotatedFirstField = Arrays.stream(entityClass
                        .getDeclaredFields())
                        .filter(e -> e.isAnnotationPresent(Id.class))
                        .findFirst();
                annotatedFirstField.ifPresent(field -> result.put(entityClass, field));
            }
        }
        return result;
    }



    public static  Map<Class, Set<Field>> findAllColumnNamesForEntities(Set<Class<?>> entityClasses){
        Map<Class, Set<Field>> result = new HashMap<>();
        for (Class entityClass : entityClasses) {
            if(entityClass.isAnnotationPresent(Entity.class)){
                Field[] declaredFields = entityClass.getDeclaredFields();
                result.put(entityClass, new HashSet<>(Arrays.asList(declaredFields)));
            }
        }
        return result;
    }

    public static Optional<Field> findPrimaryKeyForSelectedEntityClass(Class<?> clazz){
        if(clazz.isAnnotationPresent(Entity.class)){
            return Arrays.stream(clazz.getDeclaredFields()).filter(e -> e.isAnnotationPresent(Id.class)).findFirst();
        }else{
            return Optional.empty();
        }

    }
}
