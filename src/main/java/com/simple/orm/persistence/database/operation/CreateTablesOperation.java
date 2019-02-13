package com.simple.orm.persistence.database.operation;


import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CreateTablesOperation implements TablesOperation {


    public String prepareQuery(Class e) {
        String query = "CREATE TABLE IF NOT EXISTS ";
        Entity entity = (Entity) e.getAnnotation(Entity.class);
        String entityName = entity.name();
        if (entityName.equals("")) {
            query = query.concat(e.getSimpleName());
        } else {
            query = query.concat(entity.name());
        }
        query = query.concat(" ( ");
        Field[] fields = e.getDeclaredFields();
        for (Field f : fields) {
            String columnType = getFieldDatabaseType(f.getType().getTypeName());
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                if (column.name().equals("")) {
                    query = query.concat(f.getName() + " " + columnType + ", ");
                } else {
                    query = query.concat(column.name() + " " + columnType + ", ");
                }
            } else {
                query = query.concat(f.getName() + " " + columnType + ", ");
            }
        }
        query = query.substring(0, query.length() - 2);
        query = query.concat(" );");
        return query;
    }

    private String getFieldDatabaseType(String fieldType) {
        if (fieldType.equals(String.class.getTypeName())) {
            return "TEXT";
        } else if (fieldType.equals(Integer.class.getTypeName())) {
            return "INT";
        } else if (fieldType.equals(Long.class.getTypeName())) {
            return "MEDIUMINT";
        } else if (fieldType.equals(BigDecimal.class.getTypeName())) {
            return "DOUBLE";
        } else if (fieldType.equals(BigInteger.class.getTypeName())) {
            return "BIGINT";
        } else if (fieldType.equals(Date.class.getTypeName()) || fieldType.equals(LocalDate.class.getTypeName())) {
            return "DATE";
        } else if (fieldType.equals(LocalDateTime.class.getTypeName())) {
            return "DATETIME";
        } else if (fieldType.equals(Timestamp.class.getTypeName())) {
            return "TIMESTAMP";
        }
        return null;
    }


}
