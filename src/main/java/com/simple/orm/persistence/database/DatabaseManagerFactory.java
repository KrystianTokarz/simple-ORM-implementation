package com.simple.orm.persistence.database;

class DatabaseManagerFactory {
    static DatabaseManager get(String database) {
        if(database.equals("mysql")){
            return new DatabaseManagerMySql();
        } else {
            throw new RuntimeException("Nieobslugiwana baza danych");
        }
    }
}
