package com.simple.orm.persistence.entity.operation;

import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import com.simple.orm.utils.FieldValueForSqlOperationsPreparer;

import java.util.Map;

public class SelectLastIdValueOperation implements SqlOperation {

    private String entityClassName;
    private Map<String, Object> properties;

    public SelectLastIdValueOperation(String entityClassName, Map<String,Object > properties) {
        this.entityClassName = entityClassName;
        this.properties = properties;
    }

    @Override
    public String generateSql() {
        Object idFieldName = properties.get(IdentificationField.ID_FIELD_NAME.getValue());

        return new StringBuilder()
                .append("SELECT MAX(")
                .append(idFieldName.toString())
                .append(") FROM ")
                .append(entityClassName).toString();
    }
}
