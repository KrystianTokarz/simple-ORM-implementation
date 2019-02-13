package com.simple.orm.persistence.entity.cache;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EntityMangerCache {

    private static EntityMangerCache entityMangerCache;

    private Map<Class, Map<Number, Object>> cachedEntity = new HashMap<>();

    private EntityMangerCache() {
    }

    public static EntityMangerCache instance() {
        if (entityMangerCache == null) {
            return new EntityMangerCache();
        } else {
            return entityMangerCache;
        }
    }


    public Optional<Object> get(Class K, Number id) {
        Map<Number, Object> numberObjectMap = cachedEntity.get(K);
        Object o = numberObjectMap.get(K);
        return Optional.of(o);
    }

//
//    public Object removeFromCache(Class clazz, Number id) {
//        synchronized (cachedEntity) {
//            Map<Number, Object> numberCachedEntityMap = cachedEntity.get(clazz);
//            return numberCachedEntityMap.remove(id);
//        }
//    }

    public void addToCache(Number id, Object object) {
        synchronized (cachedEntity) {
            Map<Number, Object> numberCachedEntityMap = cachedEntity.get(object.getClass());
            numberCachedEntityMap.put(id, object);
        }
    }

    public boolean isEmpty() {
        return this.cachedEntity.isEmpty();
    }


    public void clearCache() {
        this.cachedEntity.clear();
    }

    public void flush() throws SQLException {

    }


}
