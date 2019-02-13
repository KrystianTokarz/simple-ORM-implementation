package com.simple.orm.utils;

public class GenericClass<T> {

    private final Class<T> type;
    private final Object object;

    public GenericClass(Class<T> type, Object object) {
        this.type = type;
        this.object = object;
    }

    public Class<T> getMyType() {
        return this.type;
    }

    public Object getMyObject(){
        return this.object;
    }
}
