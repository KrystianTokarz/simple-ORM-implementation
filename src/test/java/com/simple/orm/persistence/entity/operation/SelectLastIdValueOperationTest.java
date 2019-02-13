package com.simple.orm.persistence.entity.operation;

import com.simple.orm.testentity.TestEntity;
import com.simple.orm.persistence.entity.operation.fields.IdentificationField;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectLastIdValueOperationTest {


    @Test
    public void shouldPrepareSelectLastSqlQuery() throws Exception{

        //given
        Long id = 1L;
        String idFieldName = "id";
        TestEntity testEntity = new TestEntity();

        Map<String, Object> properties = new HashMap<>();
        properties.put(IdentificationField.ID_FIELD_VALUE.getValue(), id.toString());
        properties.put(IdentificationField.ID_FIELD_NAME.getValue(), idFieldName);

        SqlOperation selectOperation = new SelectLastIdValueOperation(testEntity.getClass().getSimpleName(), properties);

        //when
        String result = selectOperation.generateSql();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).containsIgnoringCase("SELECT MAX("+idFieldName+ ")");
        assertThat(result).containsIgnoringCase("FROM");
        assertThat(result).contains(testEntity.getClass().getSimpleName());

    }
}