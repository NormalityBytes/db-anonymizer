package com.spirittesting.db.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public record Table(
        TableId descriptor,
        String type,
        String remarks,
        String typeCat,
        String typeSchem,
        String typeName,
        String selfReferencingColName,
        String refGeneration
) implements Comparable<Table> {

    @Override
    public int compareTo(Table o) {
        return descriptor.compareTo(o.descriptor);
    }

    public static Set<Table> getTables(Connection connection, String... types) {
        Set<Table> tables = new HashSet<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, types);
            while (resultSet.next()) {
                if (Arrays.stream(types).noneMatch(resultSet.getString("TABLE_TYPE")::equalsIgnoreCase)) continue;
                tables.add(new Table(
                        new TableId(
                                resultSet.getString("TABLE_CAT"),
                                resultSet.getString("TABLE_SCHEM"),
                                resultSet.getString("TABLE_NAME")
                        ),
                        resultSet.getString("TABLE_TYPE"),
                        resultSet.getString("REMARKS"),
                        resultSet.getString("TYPE_CAT"),
                        resultSet.getString("TYPE_SCHEM"),
                        resultSet.getString("TYPE_NAME"),
                        resultSet.getString("SELF_REFERENCING_COL_NAME"),
                        resultSet.getString("REF_GENERATION")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

}
