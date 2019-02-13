package com.simple.orm.utils;

import com.simple.orm.testentity.TestEntity;
import org.testng.annotations.Test;

import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class EntityFinderTest {

    @Test
    public void shouldFindTestClassesEntityInProject(){
        //when
        Set<Class<?>> result = EntityFinder.findEntities();
        //then
        assertThat(result).contains(TestEntity.class);
    }

}