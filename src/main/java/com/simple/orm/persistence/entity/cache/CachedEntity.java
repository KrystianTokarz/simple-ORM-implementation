package com.simple.orm.persistence.entity.cache;

public class CachedEntity {

    public Object entity;
    public CacheOperation cacheOperation;

    public CachedEntity(Object entity, CacheOperation cacheOperation) {
        this.entity = entity;
        this.cacheOperation = cacheOperation;
    }
}
