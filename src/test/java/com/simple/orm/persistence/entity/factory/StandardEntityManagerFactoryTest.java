package com.simple.orm.persistence.entity.factory;

import com.simple.orm.persistence.entity.manager.StandardEntityManager;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardEntityManagerFactoryTest {


    @Test
    public void shouldCreateEntityManager() throws Exception {
        //given
        StandardEntityManagerFactory entityManagerFactory = new StandardEntityManagerFactory();

        //when
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        //then
        assertThat(entityManager).isNotNull();
        assertThat(entityManager.getClass()).hasSameClassAs(StandardEntityManager.class);
        assertThat(entityManagerFactory.entityManagerPool).isNotEmpty();
        assertThat(entityManagerFactory.entityManagerPool).hasSize(1);
        assertThat(entityManagerFactory.entityManagerPool).contains(entityManager);
    }

    @Test
    public void shouldCorrectlyCloseDatabaseConnection(){
        //given
        StandardEntityManagerFactory entityManagerFactory = new StandardEntityManagerFactory();

        //when
        entityManagerFactory.close();

        //then
        assertThat(entityManagerFactory.isOpen()).isFalse();
        assertThat(entityManagerFactory.entityManagerPool).isEmpty();
    }

    @Test(expectedExceptions = RuntimeException.class )
    public void shouldThrowExceptionWhileCreateEntityManagerWhichIsAlreadyClosed() throws Exception {
        //given
        StandardEntityManagerFactory entityManagerFactory = new StandardEntityManagerFactory();

        //when
        entityManagerFactory.close();
        entityManagerFactory.createEntityManager();
    }

    @Test
    public void shouldReturnCorrectlyStatusOfEntityManagerFactory() throws Exception{
        //given
        StandardEntityManagerFactory entityManagerFactory = new StandardEntityManagerFactory();

        //when
        boolean firstValue = entityManagerFactory.isOpen();
        entityManagerFactory.close();
        boolean secondValue = entityManagerFactory.isOpen();

        //then
        assertThat(firstValue).isTrue();
        assertThat(secondValue).isFalse();
    }
}