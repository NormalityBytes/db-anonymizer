package com.spirittesting.db.model;

import java.util.Optional;

public record ColumnDefinition(
        ColumnDescriptor descriptor,
        int dataType,
        String typeName,
        int columnSize,
        int decimalDigits,
        int numPrecRadix,
        Optional<Boolean> nullable,
        String remarks,
        String columnDef,
        int charOctetLength,
        int ordinalPosition,
        String scopeCatalog, String scopeSchema, String scopeTable, int shortDataType,
        Optional<Boolean> autoIncrement,
        Optional<Boolean> generatedColumn

) implements Comparable<ColumnDefinition> {

    @Override
    public int compareTo(ColumnDefinition o) {
        return descriptor.compareTo(o.descriptor);
    }
}
