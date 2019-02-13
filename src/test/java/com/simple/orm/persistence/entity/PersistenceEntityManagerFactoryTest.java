package com.simple.orm.persistence.entity;

import com.simple.orm.persistence.entity.factory.StandardEntityManagerFactory;
import org.testng.annotations.Test;

import javax.persistence.EntityManagerFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testng.Assert.assertNotNull;

public class PersistenceEntityManagerFactoryTest {


    @Test
    public void shouldGetEntityManagerFactory() throws Exception {

        //when
        EntityManagerFactory entityManagerFactory = PersistenceEntityManagerFactory.createEntityManagerFactory();

        //then
        assertNotNull(entityManagerFactory);
        assertThat(entityManagerFactory.getClass()).hasSameClassAs(StandardEntityManagerFactory.class);
    }

}