package com.simple.orm.persistence.entity.operation;

import com.simple.orm.utils.FieldValueForSqlOperationsPreparer;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class InsertOperation implements SqlOperation {

    private String entityClassName;
    private Map<String,Object> properties;

    public InsertOperation(String entityClassName, Map<String,Object> properties) {
        this.entityClassName = entityClassName;
        this.properties = properties;
    }

    @Override
    public String generateSql(){
        String keyValues = properties.keySet().stream().collect(Collectors.joining(",", "(", ")"));
        String propertiesValues = properties.entrySet().stream().map(e ->
            FieldValueForSqlOperationsPreparer.prepare(e.getValue())).collect(Collectors.joining(",", "(", ")"));
        return new StringJoiner(" ")
                .add("INSERT INTO")
                .add(entityClassName)
                .add(keyValues)
                .add("VALUES")
                .add(propertiesValues).toString();
    }
}
