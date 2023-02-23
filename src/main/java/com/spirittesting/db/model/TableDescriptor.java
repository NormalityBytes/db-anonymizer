package com.spirittesting.db.model;

import java.util.Objects;

public record TableDescriptor (
        String catalog,
        String schema,
        String table
) implements Comparable<TableDescriptor> {
    @Override
    public int compareTo(TableDescriptor o) {
        if (!Objects.equals(catalog, o.catalog)) return catalog.compareTo(o.catalog);
        if (!Objects.equals(schema, o.schema)) return schema.compareTo(o.schema);
        return table.compareTo(o.table);
    }
}
