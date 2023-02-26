package com.spirittesting.db.database;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public record ForeignKey(
        ColumnId primaryKey,
        ColumnId foreignKey,
        int keySeq,
        UpdateRule updateRule,
        UpdateRule deleteRule,
        String fkName,
        String pkName,
        Deferrability deferrability
) {

    public static Set<ForeignKey> getForeignKeys(Connection connection, TableId tableId) {
        Set<ForeignKey> foreignKeys = new HashSet<>();
        try (ResultSet resultSet = connection.getMetaData().getImportedKeys(
                tableId.catalog(),
                tableId.schema(),
                tableId.table()
        )) {
            while (resultSet.next()) {
                foreignKeys.add(new ForeignKey(
                        new ColumnId(
                                new TableId(
                                        resultSet.getString("PKTABLE_CAT"),
                                        resultSet.getString("PKTABLE_SCHEM"),
                                        resultSet.getString("PKTABLE_NAME")
                                ),
                                resultSet.getString("PKCOLUMN_NAME")
                        ),
                        new ColumnId(
                                new TableId(
                                        resultSet.getString("FKTABLE_CAT"),
                                        resultSet.getString("FKTABLE_SCHEM"),
                                        resultSet.getString("FKTABLE_NAME")
                                ),
                                resultSet.getString("FKCOLUMN_NAME")
                        ),
                        resultSet.getShort("KEY_SEQ"),
                        UpdateRule.fromValue(resultSet.getShort("UPDATE_RULE")),
                        UpdateRule.fromValue(resultSet.getShort("DELETE_RULE")),
                        resultSet.getString("FK_NAME"),
                        resultSet.getString("PK_NAME"),
                        Deferrability.fromValue(resultSet.getShort("DEFERRABILITY"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return foreignKeys;
    }

}
