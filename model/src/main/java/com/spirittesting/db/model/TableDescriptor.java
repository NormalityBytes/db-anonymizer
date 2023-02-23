package com.spirittesting.db.model;

public record TableDescriptor(
        String catalog,
        String schema,
        String table
) {
}
