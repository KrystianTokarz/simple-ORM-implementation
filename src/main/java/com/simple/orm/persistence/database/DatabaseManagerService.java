package com.simple.orm.persistence.database;

import com.simple.orm.singleton.PropertiesLoader;

public class DatabaseManagerService {

    private static DatabaseManagerService databaseManagerService;
    private DatabaseManager queryManager;

    private DatabaseManagerService(){
        queryManager = DatabaseManagerFactory.get(PropertiesLoader.getInstance().getProperty("database.name"));
    }

    public static DatabaseManager getQueryManagerInstance(){
        if(databaseManagerService == null){
            databaseManagerService = new DatabaseManagerService();
        }
        return databaseManagerService.getQueryManager();
    }

    private DatabaseManager getQueryManager(){
        return queryManager;
    }
}
