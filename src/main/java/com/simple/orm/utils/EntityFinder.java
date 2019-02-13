package com.simple.orm.utils;

import org.reflections.Reflections;

import javax.persistence.Entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityFinder {

    private static final String PROJECT_PACKAGE_PREFIX = "com.simple.orm";
//
//    public static List<Class> findEntities(List<Class> classes) {
//        return classes.stream()
//                .filter(c -> c.isAnnotationPresent(Entity.class))
//                .collect(Collectors.toList());
//    }


    public static Set<Class<?>> findEntities(){
        Reflections reflections = new Reflections(PROJECT_PACKAGE_PREFIX);
        return  reflections.getTypesAnnotatedWith(javax.persistence.Entity.class);
    }

}
