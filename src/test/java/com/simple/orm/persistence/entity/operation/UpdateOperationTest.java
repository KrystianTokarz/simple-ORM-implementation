package com.simple.orm.persistence.entity.operation;

import com.simple.orm.testentity.TestEntity;
import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateOperationTest {

    @Test
    public void shouldPrepareUpdateSqlQuery() throws Exception{

        //given
        Long id = 1L;
        String idFieldName = "id";

        Integer year = 20;
        String lastName = "lastName";
        String firstName = "firstName";
        String email = "email@email.email";

        String firstNameFieldName = "firstNameFieldName";
        String lastNameFieldName = "lastNameFieldName";
        String emailFieldName = "emailFieldName";
        String yearFieldName = "yearFieldName";

        TestEntity testEntity = new TestEntity();

        Map<String, Object> properties = new HashMap<>();
        properties.put(IdentificationField.ID_FIELD_VALUE.getValue(), id.toString());
        properties.put(IdentificationField.ID_FIELD_NAME.getValue(), idFieldName);
        properties.put(yearFieldName,year);
        properties.put(firstNameFieldName, firstName);
        properties.put(lastNameFieldName, lastName);
        properties.put(emailFieldName, email);

        SqlOperation sqlOperation = new UpdateOperation(testEntity.getClass().getSimpleName(),properties);

        //when
        String result = sqlOperation.generateSql();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsIgnoringCase("UPDATE");
        assertThat(result).containsIgnoringCase(testEntity.getClass().getSimpleName());
        assertThat(result).containsIgnoringCase("SET");
        assertThat(result).containsIgnoringCase("WHERE");
        assertThat(result).contains(idFieldName + " = " + id);
    }


}