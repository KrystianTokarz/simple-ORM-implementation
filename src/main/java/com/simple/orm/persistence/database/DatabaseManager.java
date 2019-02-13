package com.simple.orm.persistence.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {

    void createTables( Set<Class<?>> entities) throws SQLException;
}
