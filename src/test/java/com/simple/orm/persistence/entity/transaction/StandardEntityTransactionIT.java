package com.simple.orm.persistence.entity.transaction;

import com.simple.orm.singleton.PropertiesLoader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Java6Assertions.assertThat;

;

public class StandardEntityTransactionIT {

    private static final String DB_PROPERTY_VALUE = "database.url";
    private Connection connection;
    private Statement statement;

    @BeforeTest
    public void initialize() throws SQLException {
        connection = DriverManager.getConnection(PropertiesLoader.getInstance().getProperty(DB_PROPERTY_VALUE));

    }

    @BeforeMethod
    public void init() throws Exception {
        this.statement = this.connection.createStatement();
    }

    @AfterMethod
    public void clear() throws Exception {
        statement.close();
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    private void shouldThrowExceptionWhenConnectionIsNotExist() throws Exception {
        //given
        Statement statement = null;

        //when
        StandardEntityTransaction.from(statement);
    }

    @Test
    public void shouldCorrectlyBeginTransaction() throws Exception {
        //when

        StandardEntityTransaction standardEntityTransaction = StandardEntityTransaction.from(statement);
        standardEntityTransaction.begin();

        standardEntityTransaction.commit();

        //then
        assertThat(statement).isNotNull();
    }

    @Test
    public void shouldCorrectlyCommitTransaction() throws Exception {

        //when
        StandardEntityTransaction standardEntityTransaction = StandardEntityTransaction.from(statement);
        standardEntityTransaction.begin();
        standardEntityTransaction.commit();

        //then
        assertThat(statement.getConnection().getAutoCommit()).isTrue();
    }


    @Test
    public void shouldCorrectlyRollbackTransaction() throws Exception {

        //when
        StandardEntityTransaction standardEntityTransaction = StandardEntityTransaction.from(statement);
        standardEntityTransaction.begin();
        standardEntityTransaction.rollback();

        //then
        assertThat(statement.getConnection().getAutoCommit()).isTrue();
    }


    @Test
    public void shouldCorrectlyMarkTransactionAsRollback() throws Exception {

        //when
        StandardEntityTransaction standardEntityTransaction = StandardEntityTransaction.from(statement);

        standardEntityTransaction.begin();
        standardEntityTransaction.setRollbackOnly();
        assertThat(standardEntityTransaction.getRollbackOnly()).isTrue();

        standardEntityTransaction.commit();
        assertThat(statement.getConnection().getAutoCommit()).isTrue();
    }

    @Test
    public void shouldCorrectlyCheckThatTransactionIsActive() throws Exception {

        //when
        StandardEntityTransaction standardEntityTransaction = StandardEntityTransaction.from(statement);
        standardEntityTransaction.begin();
        boolean beforeCommit = standardEntityTransaction.isActive();
        standardEntityTransaction.commit();
        boolean afterCommit = standardEntityTransaction.isActive();

        //then
        assertThat(beforeCommit).isTrue();
        assertThat(afterCommit).isFalse();


    }


}