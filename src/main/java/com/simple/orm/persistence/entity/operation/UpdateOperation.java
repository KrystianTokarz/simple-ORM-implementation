package com.simple.orm.persistence.entity.operation;

import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import com.simple.orm.utils.FieldValueForSqlOperationsPreparer;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class UpdateOperation implements SqlOperation {


    private String entityClassName;
    private Map<String, Object> properties;

    public UpdateOperation(String entityClassName, Map<String, Object> properties) {
        this.entityClassName = entityClassName;
        this.properties = properties;
    }

    @Override
    public String generateSql() {
        Object idFieldName = properties.remove(IdentificationField.ID_FIELD_NAME.getValue());
        Object idFieldValue = properties.remove(IdentificationField.ID_FIELD_VALUE.getValue());

        String updateValues = properties.entrySet().stream().map(e -> {
            String key = e.getKey();
            String value = FieldValueForSqlOperationsPreparer.prepare(e.getValue());
            return key + " = " + value;
        }).collect(Collectors.joining(" , ", " ", " "));

        return new StringJoiner(" ")
                .add("UPDATE")
                .add(entityClassName)
                .add("SET")
                .add(updateValues)
                .add("WHERE")
                .add(idFieldName.toString())
                .add("=")
                .add(idFieldValue.toString()).toString();
    }
//
//    @Override
//    public <T> T execute() throws SQLException {
//        String sql = generateSql();
//        Connection databaseConnection = super.getDatabaseConnection();
//        Statement statement = databaseConnection.createStatement();
//        int result = statement.executeUpdate(sql);
//        if(result== 0 ){
//            throw new SQLException();
//        }
//        return null;
//    }
}
