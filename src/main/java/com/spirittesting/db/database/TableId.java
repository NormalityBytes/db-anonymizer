package com.spirittesting.db.database;

import java.util.Objects;

public record TableId(String catalog, String schema, String table) implements Comparable<TableId> {

    @Override
    public int compareTo(TableId o) {
        if (!Objects.equals(catalog, o.catalog)) return catalog.compareTo(o.catalog);
        if (!Objects.equals(schema, o.schema)) return schema.compareTo(o.schema);
        return table.compareTo(o.table);
    }

    @Override
    public String toString() {
        String result = table;
        if (schema != null) result = schema + "." + result;
        if (catalog != null) result = catalog + "." + result;
        return result;
    }
}
