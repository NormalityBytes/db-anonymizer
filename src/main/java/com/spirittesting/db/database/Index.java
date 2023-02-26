package com.spirittesting.db.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public record Index(
        TableId table,
        boolean nonUnique,
        String indexQualifier,
        String indexName,
        IndexType type,
        int ordinalPosition,
        String columnName,
        SortOrder ascOrDesc,
        int cardinality,
        int pages,
        String filterCondition) {

    public static Set<Index> getIndices(Connection connection, TableId table) {
        Set<Index> indices = new HashSet<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getIndexInfo(table.catalog(), table.schema(), table.table(), false, false);
            while (resultSet.next()) {
                indices.add(new Index(
                        new TableId(
                                resultSet.getString("TABLE_CAT"),
                                resultSet.getString("TABLE_SCHEM"),
                                resultSet.getString("TABLE_NAME")),
                        resultSet.getBoolean("NON_UNIQUE"),
                        resultSet.getString("INDEX_QUALIFIER"),
                        resultSet.getString("INDEX_NAME"),
                        IndexType.fromValue(resultSet.getShort("TYPE")),
                        resultSet.getShort("ORDINAL_POSITION"),
                        resultSet.getString("COLUMN_NAME"),
                        SortOrder.fromValue(resultSet.getString("ASC_OR_DESC")),
                        resultSet.getInt("CARDINALITY"),
                        resultSet.getInt("PAGES"),
                        resultSet.getString("FILTER_CONDITION")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indices;
    }

}
