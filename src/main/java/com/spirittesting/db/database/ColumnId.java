package com.spirittesting.db.database;

public record ColumnId(TableId tableId, String column) implements Comparable<ColumnId> {
    @Override
    public int compareTo(ColumnId o) {
        if (o.tableId().compareTo(tableId()) != 0) {
            return o.tableId().compareTo(tableId());
        }
        return o.column().compareTo(column());
    }
}
