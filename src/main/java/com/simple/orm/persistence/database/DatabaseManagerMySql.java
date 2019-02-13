package com.simple.orm.persistence.database;


import com.simple.orm.persistence.DatabaseConnection;
import com.simple.orm.persistence.database.operation.CreateTablesOperation;
import com.simple.orm.persistence.database.operation.TablesOperation;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

class DatabaseManagerMySql extends DatabaseConnection implements DatabaseManager {


    public void createTables( Set<Class<?>> entities) throws SQLException {
        TablesOperation createTablesOperation = new CreateTablesOperation();
        Statement statement = super.createStatement();
        for (Class entity : entities) {
            String query = createTablesOperation.prepareQuery(entity);
            statement.executeUpdate(query);
        }
    }

}
