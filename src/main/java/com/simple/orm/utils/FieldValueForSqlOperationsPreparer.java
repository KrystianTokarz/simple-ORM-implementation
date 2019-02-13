package com.simple.orm.utils;

import java.util.Optional;

public class FieldValueForSqlOperationsPreparer {

    private static final String APOSTROPHE_VALUE = "'";
    private static final String NULL_VALUE = "NULL";

    public static String prepare(Object value){
        if( value != null){
            if(value instanceof String){
                return APOSTROPHE_VALUE + value.toString() + APOSTROPHE_VALUE;
            }else{
                return value.toString();
            }
        } else{
            return NULL_VALUE;
        }
    }
}
