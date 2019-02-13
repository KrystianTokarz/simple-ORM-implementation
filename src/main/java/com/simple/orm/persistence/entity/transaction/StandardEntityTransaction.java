package com.simple.orm.persistence.entity.transaction;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Statement.SUCCESS_NO_INFO;

public class StandardEntityTransaction implements EntityTransaction {

    private Statement statement;
    private boolean isRollback = false;

    public static StandardEntityTransaction from(Statement statement ) {
        validate(statement);
        return new StandardEntityTransaction(statement);
    }

    private static void validate(Statement statement) {
        if (statement == null) {
            throw new IllegalArgumentException("statement cannot be null");
        }
    }

    private StandardEntityTransaction(Statement statement) {
        this.statement = statement;
    }

    @Override
    public void begin() {
        throwIllegalArgumentExceptionIfTransactionActiveHasStatus(true);
        try {
            this.statement.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        throwIllegalArgumentExceptionIfTransactionActiveHasStatus(false);
        if (this.isRollback) {
            this.rollback();
        } else {
            commitAllOperations();
        }
    }

    private void commitAllOperations() {
        try {
            int[] results = statement.executeBatch();
            checkExecuteBatchWasSuccessfully(results);
            this.statement.getConnection().commit();
            this.statement.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throwRollbackException();
        }
    }

    private void checkExecuteBatchWasSuccessfully(int[] results) throws SQLException {
        for (int result : results) {
            throwExceptionWhenOperationHasError(result);
        }
    }

    private void throwExceptionWhenOperationHasError(int operationResult) throws SQLException {
        if (operationResult < 0 && operationResult != SUCCESS_NO_INFO) {
            this.rollback();
            throw new SQLException("Commit operation generate error");
        }
    }

    @Override
    public void rollback() {
        throwIllegalArgumentExceptionIfTransactionActiveHasStatus(false);
        try {
            this.statement.getConnection().rollback();
            this.statement.getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throwPersistenceExceptionForUnexpectedError();
        }
    }

    @Override
    public void setRollbackOnly() {
        throwIllegalArgumentExceptionIfTransactionActiveHasStatus(false);
        this.isRollback = true;
    }

    @Override
    public boolean getRollbackOnly() {
        throwIllegalArgumentExceptionIfTransactionActiveHasStatus(false);
        return this.isRollback;
    }

    @Override
    public boolean isActive() {
        boolean isActive;
        try {
            isActive = checkTransactionIsActive();
        } catch (SQLException e) {
            isActive = false;
            throwPersistenceExceptionForUnexpectedError();
        }
        return isActive;
    }

    private boolean checkTransactionIsActive() throws SQLException {
        return !this.statement.getConnection().getAutoCommit();
    }

    private void throwIllegalArgumentExceptionIfTransactionActiveHasStatus(boolean status) {
        if (this.isActive() == status) {
            throw new IllegalStateException("Unexpected error for transaction status");
        }
    }

    private void throwPersistenceExceptionForUnexpectedError() {
        throw new PersistenceException("Unexpected error condition is encountered");
    }

    private void throwRollbackException() {
        throw new RollbackException("Commit fails");
    }
}
