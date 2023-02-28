package com.spirittesting.db.frontend.components;

import com.spirittesting.db.database.Column;
import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ForeignKey;
import com.spirittesting.db.database.Index;
import com.spirittesting.db.database.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public record TableColumnModelItem(
        Column column,
        Set<Index> indices,
        Set<ForeignKey> foreignKeys
) {
    private static final Logger log = Logger.getLogger("TableColumnModelItem");

    public String toString() {
        return column.descriptor().column();
    }

    public static Set<TableColumnModelItem> fromTable(Table table) {
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            Set<TableColumnModelItem> tableColumnModelItems = new HashSet<>();
            Set<Column> columns = Column.getColumns(connection, table.descriptor());
            Set<Index> indices = Index.getIndices(connection, table.descriptor());
            Set<ForeignKey> foreignKeys = ForeignKey.getForeignKeys(connection, table.descriptor());

            for (Column column : columns) {
                Set<Index> columnIndices = new HashSet<>();
                Set<ForeignKey> columnForeignKeys = new HashSet<>();
                for (Index index : indices) {
                    if (index.columnName().equalsIgnoreCase(column.descriptor().column())) {
                        columnIndices.add(index);
                    }
                }
                for (ForeignKey foreignKey : foreignKeys) {
                    if (foreignKey.foreignKey().equals(column.descriptor())) {
                        columnForeignKeys.add(foreignKey);
                    }
                    if (foreignKey.primaryKey().equals(column.descriptor())) {
                        columnForeignKeys.add(foreignKey);
                    }
                }
                tableColumnModelItems.add(new TableColumnModelItem(column, columnIndices, columnForeignKeys));
            }

            return tableColumnModelItems;
        } catch (SQLException e) {
            log.severe("Error while creating TableColumnModelItem: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
