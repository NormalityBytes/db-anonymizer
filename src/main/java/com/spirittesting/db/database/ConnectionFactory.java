package com.spirittesting.db.database;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class ConnectionFactory {

    private static ConnectionFactory instance;

    private ConnectionFactory() {
    }

    @Synchronized
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    @Getter
    @Setter
    private String jdbc;
    @Getter
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
            log.error("Fehler beim Erstellen der Datenbankverbindung", e);
            throw new RuntimeException(e);
        }
    }


}
