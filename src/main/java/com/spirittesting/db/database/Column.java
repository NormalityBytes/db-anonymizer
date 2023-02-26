package com.spirittesting.db.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record Column(
        ColumnId descriptor,
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

) implements Comparable<Column> {

    @Override
    public int compareTo(Column o) {
        return descriptor.compareTo(o.descriptor);
    }

    public static Set<Column> getColumns(Connection connection, TableId table) {
        Set<Column> columns = new HashSet<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(table.catalog(), table.schema(), table.table(), null);
            while (resultSet.next()) {
                columns.add(new Column(
                        new ColumnId(
                                new TableId(
                                        resultSet.getString("TABLE_CAT"),
                                        resultSet.getString("TABLE_SCHEM"),
                                        resultSet.getString("TABLE_NAME")
                                ),
                                resultSet.getString("COLUMN_NAME")
                        ),
                        resultSet.getInt("DATA_TYPE"),
                        resultSet.getString("TYPE_NAME"),
                        resultSet.getInt("COLUMN_SIZE"),
                        resultSet.getInt("DECIMAL_DIGITS"),
                        resultSet.getInt("NUM_PREC_RADIX"),
                        switch (resultSet.getInt("NULLABLE")) {
                            case DatabaseMetaData.columnNoNulls -> Optional.of(false);
                            case DatabaseMetaData.columnNullable -> Optional.of(true);
                            default -> Optional.empty();
                        },
                        resultSet.getString("REMARKS"),
                        resultSet.getString("COLUMN_DEF"),
                        resultSet.getInt("CHAR_OCTET_LENGTH"),
                        resultSet.getInt("ORDINAL_POSITION"),
                        resultSet.getString("SCOPE_CATALOG"),
                        resultSet.getString("SCOPE_SCHEMA"),
                        resultSet.getString("SCOPE_TABLE"),
                        resultSet.getInt("SOURCE_DATA_TYPE"),
                        switch (resultSet.getString("IS_AUTOINCREMENT")) {
                            case "YES" -> Optional.of(true);
                            case "NO" -> Optional.of(false);
                            default -> Optional.empty();
                        },
                        switch (resultSet.getString("IS_GENERATEDCOLUMN")) {
                            case "YES" -> Optional.of(true);
                            case "NO" -> Optional.of(false);
                            default -> Optional.empty();
                        }
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }
}
