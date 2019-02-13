package com.simple.orm.testentity;

public class TestEntityBuilder {


    public static final String TEST_EMAIL_VALUE = "test@test.test";
    public static final String FIRST_NAME_VALUE = "firstname";
    public static final String LAST_NAME_VALUE = "lastname";
    public static final Integer TEST_YEAR_VALUE = 30;


    public static TestEntity buildTestEntityWithoutId() {
        TestEntity testEntity = new TestEntity();
        testEntity.setEmial(TEST_EMAIL_VALUE);
        testEntity.setFirstName(FIRST_NAME_VALUE);
        testEntity.setLastName(LAST_NAME_VALUE);
        testEntity.setYear(TEST_YEAR_VALUE);
        return testEntity;
    }

    public static TestEntity buildTestEntity(Long id){
        TestEntity testEntity = buildTestEntityWithoutId();
        testEntity.setId(id);
        return testEntity;
    }
}
