package com.spirittesting.db.model;

public record ColumnDescriptor(
        TableDescriptor table,
        String column
) implements Comparable<ColumnDescriptor> {

    @Override
    public int compareTo(ColumnDescriptor o) {
        int result = table.compareTo(o.table);
        if (result != 0) return result;
        return column.compareTo(o.column);
    }
}
