package com.spirittesting.db.model;

public record TableDefinition(
        TableDescriptor descriptor,
        String type,
        String remarks,
        String typeCat,
        String typeSchem,
        String typeName,
        String selfReferencingColName,
        String refGeneration
) implements Comparable<TableDefinition> {

    @Override
    public int compareTo(TableDefinition o) {
        return descriptor.compareTo(o.descriptor);
    }
}
