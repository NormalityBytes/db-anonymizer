package com.spirittesting.db.database;

public record ConnectionProperty(String key, String value) {
    @Override
    public String toString() {
        return "{" + key + "=" + value + "}";
    }
}
