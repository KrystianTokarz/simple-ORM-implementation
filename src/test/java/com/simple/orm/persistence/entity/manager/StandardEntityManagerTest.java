package com.simple.orm.persistence.entity.manager;

import com.simple.orm.persistence.database.DatabaseManagerService;
import com.simple.orm.singleton.PropertiesLoader;
import com.simple.orm.testentity.TestEntity;
import com.simple.orm.testentity.TestEntityBuilder;
import com.simple.orm.utils.EntityFinder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityTransaction;
import javax.persistence.TransactionRequiredException;
import java.sql.*;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class StandardEntityManagerTest {

    private static final String DB_PROPERTY_VALUE = "database.url";
    private static final String SELECT_ALL_PREFIX = "SELECT * FROM ";
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    @BeforeTest
    public void initialize() throws SQLException {

        connection = DriverManager.getConnection(PropertiesLoader.getInstance().getProperty(DB_PROPERTY_VALUE));

        Set<Class<?>> entities = EntityFinder.findEntities();
        DatabaseManagerService.getQueryManagerInstance().createTables(entities);

    }

    @BeforeMethod
    public void initBeforeMethod() throws Exception {
        statement = connection.createStatement();
    }

    @AfterMethod
    public void clear() throws Exception {
        if (statement != null && !statement.isClosed()) {
            statement.execute("DELETE FROM " + TestEntity.class.getSimpleName());
            statement.close();
        }
        if (resultSet != null && !resultSet.isClosed()) {
            resultSet.close();
            resultSet = null;
        }
    }

    @Test
    public void shouldCreateTransaction() throws Exception {
        //given
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when and then
        EntityTransaction transaction = entityManager.getTransaction();

        assertThat(transaction).isNotNull();
        assertThat(transaction.isActive()).isFalse();

        transaction.begin();

        assertThat(transaction.isActive()).isTrue();

        transaction.commit();

        assertThat(transaction.isActive()).isFalse();
    }


    @Test
    public void shouldRollbackTransaction() throws Exception {
        //given
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when and then
        EntityTransaction transaction = entityManager.getTransaction();

        assertThat(transaction).isNotNull();
        assertThat(transaction.isActive()).isFalse();

        transaction.begin();

        assertThat(transaction.isActive()).isTrue();
        assertThat(transaction.getRollbackOnly()).isFalse();

        transaction.rollback();
        assertThat(transaction.isActive()).isFalse();
    }

    @Test
    public void shouldRollbackTransactionAfterSetRollbackFlag() throws Exception {

        //given
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when and then
        EntityTransaction transaction = entityManager.getTransaction();

        assertThat(transaction).isNotNull();
        assertThat(transaction.isActive()).isFalse();

        transaction.begin();
        transaction.setRollbackOnly();

        assertThat(transaction.isActive()).isTrue();
        assertThat(transaction.getRollbackOnly()).isTrue();

        transaction.commit();

        assertThat(transaction.isActive()).isFalse();
    }


    @Test
    public void shouldPersistObjectWithoutId() throws Exception {
        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int beforeRow = calculateResultSetRowSize(resultSet);
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.getTransaction().begin();
        entityManager.persist(testEntity);
        entityManager.getTransaction().commit();
        resultSet.close();

        //then
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int afterRow = calculateResultSetRowSize(resultSet);
        assertThat(afterRow).isEqualTo(beforeRow + 1);
    }


    @Test
    public void shouldPersistObjectWithId() throws Exception {
        //given
        Long randomIdNumber = Integer.valueOf(new Random().nextInt()).longValue();
        TestEntity testEntity = TestEntityBuilder.buildTestEntity(randomIdNumber);
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int beforeRow = calculateResultSetRowSize(resultSet);

        //whe
        StandardEntityManager entityManager = StandardEntityManager.from(statement);
        entityManager.getTransaction().begin();
        entityManager.persist(testEntity);
        entityManager.getTransaction().commit();
        resultSet.close();

        //then
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int afterRow = calculateResultSetRowSize(resultSet);
        assertThat(afterRow).isEqualTo(beforeRow + 1);
    }

    @Test(expectedExceptions = TransactionRequiredException.class)
    public void shouldThrowExceptionDuringPersistWhenTransactionNotExist() throws Exception {
        TestEntity testEntity = new TestEntity();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        entityManager.persist(testEntity);
    }


    @Test(expectedExceptions = IllegalStateException.class)
    public void shouldThrowExceptionDuringPersistWhenEntityMangerIsClosed() throws Exception {
        TestEntity testEntity = new TestEntity();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);
        entityManager.close();
        entityManager.persist(testEntity);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionDuringPersistWhenObjectIsNotAEntity() throws Exception {
        Object object = new Object();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);
        entityManager.persist(object);
    }


    @Test(expectedExceptions = EntityExistsException.class)
    public void shouldThrowExceptionDuringPersistWhenTryPersistTheSameEntity() throws Exception {
        //given
        Long randomIdNumber = Integer.valueOf(new Random().nextInt()).longValue();
        TestEntity testEntity = TestEntityBuilder.buildTestEntity(randomIdNumber);

        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.getTransaction().begin();
        entityManager.persist(testEntity);
        entityManager.persist(testEntity);
        entityManager.getTransaction().commit();
    }


    @Test
    public void shouldMergeEntityIfNotExist() throws Exception {
        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int beforeRow = calculateResultSetRowSize(resultSet);
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.getTransaction().begin();
        TestEntity mergedEntity = entityManager.merge(testEntity);
        entityManager.getTransaction().commit();
        resultSet.close();

        //then
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int afterRow = calculateResultSetRowSize(resultSet);
        assertThat(afterRow).isEqualTo(beforeRow + 1);

        assertThat(beforeRow).isLessThan(afterRow);
        assertThat(mergedEntity.getYear()).isEqualTo(testEntity.getYear());
        assertThat(mergedEntity.getLastName()).isEqualTo(testEntity.getLastName());
        assertThat(mergedEntity.getFirstName()).isEqualTo(testEntity.getFirstName());
        assertThat(mergedEntity.getEmial()).isEqualTo(testEntity.getEmial());
        assertThat(mergedEntity.getId()).isNotNull();
    }

    @Test
    public void shouldMergeEntityIfExist() throws Exception {
        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);
        String NEW_EMAIL = "NEW_EMAIL@NEW.NEW";

        //when
        entityManager.getTransaction().begin();
        entityManager.persist(testEntity);
        entityManager.getTransaction().commit();

        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int beforeRow = calculateResultSetRowSize(resultSet);
        testEntity.setEmial(NEW_EMAIL);
        entityManager.getTransaction().begin();

        TestEntity mergedEntity = entityManager.merge(testEntity);
        entityManager.getTransaction().commit();
        resultSet.close();

        //then
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int afterRow = calculateResultSetRowSize(resultSet);

        assertThat(afterRow).isEqualTo(beforeRow);
        assertThat(beforeRow).isEqualTo(afterRow);
        assertThat(mergedEntity.getYear()).isEqualTo(testEntity.getYear());
        assertThat(mergedEntity.getLastName()).isEqualTo(testEntity.getLastName());
        assertThat(mergedEntity.getFirstName()).isEqualTo(testEntity.getFirstName());
        assertThat(mergedEntity.getEmial()).isEqualTo(NEW_EMAIL);
        assertThat(mergedEntity.getId()).isNotNull();
    }

    @Test
    public void shouldSelectObject() throws Exception {
        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.getTransaction().begin();
        TestEntity mergedEntity = entityManager.merge(testEntity);
        entityManager.getTransaction().commit();
        TestEntity result = entityManager.find(mergedEntity.getClass(), mergedEntity.getId());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getEmial()).isEqualTo(TestEntityBuilder.TEST_EMAIL_VALUE);
        assertThat(result.getFirstName()).isEqualTo(TestEntityBuilder.FIRST_NAME_VALUE);
        assertThat(result.getLastName()).isEqualTo(TestEntityBuilder.LAST_NAME_VALUE);
        assertThat(result.getYear()).isEqualTo(TestEntityBuilder.TEST_YEAR_VALUE);
        assertThat(result.getId()).isNotNull().isPositive();
    }

    @Test
    public void shouldDeleteObject() throws Exception {
        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        statement = connection.createStatement();
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int beforeRow = calculateResultSetRowSize(resultSet);

        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.getTransaction().begin();
        TestEntity mergedEntity = entityManager.merge(testEntity);
        entityManager.remove(mergedEntity);
        entityManager.getTransaction().commit();

        //then
        TestEntity result = entityManager.find(mergedEntity.getClass(), mergedEntity.getId());
        assertThat(result).isNull();
        resultSet = getResultSetForSelectAllObjectInDB(testEntity);
        int afterRow = calculateResultSetRowSize(resultSet);
        assertThat(afterRow).isEqualTo(beforeRow);

    }

    @Test(expectedExceptions = TransactionRequiredException.class)
    public void shouldThrowExceptionWhenTransactionIsNotOpened() throws Exception {

        //given
        TestEntity testEntity = TestEntityBuilder.buildTestEntityWithoutId();
        StandardEntityManager entityManager = StandardEntityManager.from(statement);

        //when
        entityManager.merge(testEntity);
    }

    private int calculateResultSetRowSize(ResultSet resultSet) throws SQLException {
        int result = 0;
        while (resultSet.next()) {
            result++;
        }
        return result;
    }

    private ResultSet getResultSetForSelectAllObjectInDB(Object object) throws SQLException {
        return statement.executeQuery(SELECT_ALL_PREFIX + getSimpleClassName(object));
    }

    private String getSimpleClassName(Object object) {
        return object.getClass().getSimpleName();
    }

}