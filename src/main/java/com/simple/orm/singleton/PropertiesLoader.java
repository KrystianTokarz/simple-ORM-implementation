package com.simple.orm.singleton;


import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

    private static final String PROPERTIES_FILE_VALUE = "application.properties";

    private static PropertiesLoader propertiesLoader;
    private Properties prop;

    private PropertiesLoader(){
        prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_VALUE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesLoader getInstance(){
        if(propertiesLoader == null){
            propertiesLoader = new PropertiesLoader();
        }
        return propertiesLoader;
    }

    public String getProperty(String propertyName){
        return prop.getProperty(propertyName);
    }
}
