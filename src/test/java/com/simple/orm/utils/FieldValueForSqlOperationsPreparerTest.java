package com.simple.orm.utils;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.testng.Assert.*;

public class FieldValueForSqlOperationsPreparerTest {


    private static final String APOSTROPHE = "'";

    @Test
    public void shouldPrepareCorrectValueWithApostropheForSqlQueryForStringField() throws Exception{
        //given
        String textValue = "TEXT_VALUE";

        //when

        String result = FieldValueForSqlOperationsPreparer.prepare(textValue);

        //then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).isEqualTo(APOSTROPHE + textValue + APOSTROPHE);
    }

    @Test
    public void shouldPrepareCorrectValueWithoutApostropheForSqlQueryForFixedPointNumberField() throws Exception{
        //given
        Long numberValue = 100L;

        //when

        String result = FieldValueForSqlOperationsPreparer.prepare(numberValue);

        //then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).isEqualTo("100");
    }

    @Test
    public void shouldPrepareCorrectValueWithoutApostropheForSqlQueryForFloatingPointNumberField() throws Exception{
        //given
        int number = 100;
        BigDecimal value = new BigDecimal(number);

        //when

        String result = FieldValueForSqlOperationsPreparer.prepare(value);

        //then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result).isEqualTo("100");
    }

}