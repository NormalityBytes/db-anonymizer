package com.spirittesting.db.model;

public record ColumnDescriptor(
        TableDescriptor table,
        String column
) {
}
