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
) {
}
