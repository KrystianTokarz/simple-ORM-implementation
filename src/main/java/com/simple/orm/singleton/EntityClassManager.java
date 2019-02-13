//package com.simple.orm.singleton;
//
//import com.simple.orm.utils.AllClassFinder;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class EntityClassManager {
//
//    private static EntityClassManager entityClassManager;
//
//
//    private Set<Class> entityClasses = new HashSet<>();
//
//    private EntityClassManager() {
//        List<Class> classes = AllClassFinder.findClasses();
//        this.entityClasses.addAll(classes);
//    }
//
//
//    public static EntityClassManager instance() {
//        if (entityClassManager == null) {
//            entityClassManager = new EntityClassManager();
//        }
//        return entityClassManager;
//    }
//
//    public boolean entityClassExist(Class entityClass) {
//        return entityClasses.contains(entityClass);
//    }
//}
