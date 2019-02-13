package com.simple.orm.persistence.entity.operation;

import com.simple.orm.testentity.TestEntity;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class InsertOperationTest {



    @Test
    public void shouldPrepareInsertSqlQuery() throws Exception{

        //given
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
        properties.put(yearFieldName,year);
        properties.put(firstNameFieldName, firstName);
        properties.put(lastNameFieldName, lastName);
        properties.put(emailFieldName, email);


        SqlOperation sqlOperation = new InsertOperation(testEntity.getClass().getSimpleName(),properties);
        //when

        String result = sqlOperation.generateSql();
        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsIgnoringCase("INSERT INTO");
        assertThat(result).containsIgnoringCase(testEntity.getClass().getSimpleName());
        assertThat(result).containsIgnoringCase("VALUES");
        assertThat(result).containsIgnoringCase("(");
        assertThat(result).containsIgnoringCase(")");
    }

}