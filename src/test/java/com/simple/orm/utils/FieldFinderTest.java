package com.simple.orm.utils;

import com.simple.orm.testentity.TestEntity;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FieldFinderTest {

    @Test
    public void shouldFindPrimaryKeyFieldForEntities() throws Exception {
        //given
        Class<TestEntity> testEntityClass = TestEntity.class;
        Field idField = testEntityClass.getDeclaredField("id");

        //when
        Map<Class, Field> result = FieldFinder.findPrimaryKeyFieldForEntities(new HashSet<>(Collections.singletonList(testEntityClass)));

        //then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(testEntityClass)).isNotNull();
        assertThat(result.get(testEntityClass)).isEqualTo(idField);
    }


    @Test
    public void shouldFindAllFieldForEntities() throws Exception {
        //given
        Class<TestEntity> testEntityClass = TestEntity.class;
        Field[] declaredFields = testEntityClass.getDeclaredFields();

        //when
        Map<Class, Set<Field>> result = FieldFinder.findAllColumnNamesForEntities(new HashSet<>(Collections.singletonList(testEntityClass)));

        //then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(testEntityClass)).isNotNull().isNotEmpty();
        assertThat(result.get(testEntityClass)).hasSize(declaredFields.length);
    }

}