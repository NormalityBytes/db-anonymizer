package com.spirittesting.db.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ConnectionFactory {

    private static ConnectionFactory instance;

    private ConnectionFactory() {
    }

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    private String jdbc;
    private final Set<ConnectionProperty> connectionProperties = new HashSet<>();

    public void clearConnectionProperties() {
        connectionProperties.clear();
    }

    public void addConnectionProperty(ConnectionProperty connectionProperty) {
        connectionProperties.add(connectionProperty);
    }

    public Connection getConnection() {
        if (jdbc == null) {
            throw new IllegalStateException("JDBC-URL nicht gesetzt");
        }

        try {
            Driver driver = DatabaseType.getDriver(jdbc);
            Properties properties = new Properties();
            for (ConnectionProperty connectionProperty : connectionProperties) {
                properties.setProperty(connectionProperty.key(), connectionProperty.value());
            }
            return driver.connect(jdbc, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJdbc() {
        return jdbc;
    }

    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }

    public Set<ConnectionProperty> getConnectionProperties() {
        return connectionProperties;
    }
}
