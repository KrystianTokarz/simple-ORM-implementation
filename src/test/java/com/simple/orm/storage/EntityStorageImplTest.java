package com.simple.orm.storage;

import com.simple.orm.testentity.TestEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EntityStorageImplTest {

    private static final String TESTED_CLASS_VALUE_NAME = "entityStorage";

    private Class testEntity = TestEntity.class;

    @BeforeMethod
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = EntityStorageImpl.class.getDeclaredField(TESTED_CLASS_VALUE_NAME);
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void shouldGetSingletonObject() throws Exception{

        //given
        EntityStorage firstReturnedInstance;
        EntityStorage secondReturnedInstance;

        //when
        firstReturnedInstance = EntityStorageImpl.getInstance();
        secondReturnedInstance = EntityStorageImpl.getInstance();

        //then
        assertThat(firstReturnedInstance).isEqualTo(secondReturnedInstance);
        assertThat(firstReturnedInstance).isSameAs(secondReturnedInstance);
    }

    @Test
    public void shouldGetTestEntityClasses() throws Exception{
        //given
        EntityStorage firstReturnedInstance = EntityStorageImpl.getInstance();

        //when
        Set<Class<?>> result = firstReturnedInstance.getEntitiesClass();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).contains(testEntity);
    }

    @Test
    public void shouldGetPrimaryKeyFieldTestEntityClasses() throws Exception{
        //given
        EntityStorage firstReturnedInstance = EntityStorageImpl.getInstance();
        String primaryFieldForTestEntityName = "id";

        //when
        Field resultField = firstReturnedInstance.getPrimaryKeyFieldForEntityClass(TestEntity.class);

        //then
        assertThat(resultField).isNotNull();
        assertThat(resultField.getName()).isEqualTo(primaryFieldForTestEntityName);
    }


    @Test
    public void shouldGetAllFieldsForTestEntityClasses() throws Exception{
        //given
        EntityStorage firstReturnedInstance = EntityStorageImpl.getInstance();
        Field[] declaredFields = TestEntity.class.getDeclaredFields();

        //when
        Set<Field> allFieldForEntityClassResult = firstReturnedInstance.getAllFieldForEntityClass(TestEntity.class);

        //then
        assertThat(allFieldForEntityClassResult).isNotNull();
        assertThat(allFieldForEntityClassResult).contains(declaredFields);
    }

}