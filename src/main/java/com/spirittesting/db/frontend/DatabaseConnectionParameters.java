package com.spirittesting.db.frontend;

import com.spirittesting.db.database.ConnectionProperty;

import java.net.URL;

public record DatabaseConnectionParameters(String jdbc, java.util.List<ConnectionProperty> properties) {
    @Override
    public String toString() {
        return "JDBC URL: " + jdbc + " with properties " + properties;
    }
}
