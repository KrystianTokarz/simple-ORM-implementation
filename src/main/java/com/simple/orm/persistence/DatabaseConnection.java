package com.simple.orm.persistence;

import com.simple.orm.singleton.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseConnection {


    private static final String DB_PROPERTY_VALUE = "database.url";
    private Connection connection;


    protected DatabaseConnection(){
        try {
            connection = DriverManager.getConnection(PropertiesLoader.getInstance().getProperty(DB_PROPERTY_VALUE));
        } catch (SQLException e) {
            throw new RuntimeException("Create database connection error");
        }
    }

    private Connection getConnection(){
        if(this.connection != null && connectionIsOpen()){
            return this.connection;
        }else{
            throw new RuntimeException("Database connection is close");
        }
    }

    protected Statement createStatement(){
        Connection connection = getConnection();
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Database cannot create new statement object");
        }
    }

    protected void closeDatabaseConnection(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection close error");
        }
    }

    protected boolean connectionIsOpen(){
        try {
           return !connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Database access error occurs");
        }
    }


}
