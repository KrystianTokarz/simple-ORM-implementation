package com.simple.orm.persistence.entity.operation;


import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import com.simple.orm.utils.FieldValueForSqlOperationsPreparer;

import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class DeleteOperation implements SqlOperation  {

    private String entityClassName;
    private Map<String,Object> properties;

    public DeleteOperation(String entityClassName, Map<String,Object> properties) {
        this.entityClassName = entityClassName;
        this.properties = properties;
    }

    @Override
    public String generateSql(){
        if(properties.containsKey(IdentificationField.ID_FIELD_NAME.getValue())){
            return generateSqlWhenIdExist();
        }else{
            return generateSqlWhenIdNotExist();
        }
    }

    private String generateSqlWhenIdExist(){

        Object idFieldName = properties.get(IdentificationField.ID_FIELD_NAME.getValue());
        Object idFieldValue = properties.get(IdentificationField.ID_FIELD_VALUE.getValue());
        return new StringJoiner(" ")
                .add("DELETE FROM")
                .add(entityClassName)
                .add("WHERE")
                .add(idFieldName.toString())
                .add("=")
                .add(idFieldValue.toString()).toString();
    }

    private String generateSqlWhenIdNotExist(){
        String keyValues = properties.entrySet().stream().map(e-> e.getKey() + " = " + FieldValueForSqlOperationsPreparer.prepare(e.getValue())).collect(Collectors.joining(" AND "));
        return new StringJoiner(" ")
                .add("DELETE FROM")
                .add(entityClassName)
                .add("WHERE")
                .add(keyValues).toString();
    }


//    @Override
//    public Object execute() throws SQLException {
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
