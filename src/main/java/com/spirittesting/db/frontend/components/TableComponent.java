package com.spirittesting.db.frontend.components;

import com.spirittesting.db.database.Column;
import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ForeignKey;
import com.spirittesting.db.database.Index;
import com.spirittesting.db.database.Table;
import com.spirittesting.db.database.TableId;
import javafx.scene.control.TitledPane;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.Decorator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class TableComponent {

    private final Table table;
    private final Set<Column> columns;
    private final Set<Index> indices;
    private final Set<ForeignKey> foreignKeys;

    public TableComponent(Table table) {
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            this.table = table;
            columns = Column.getColumns(connection, table.descriptor());
            indices = Index.getIndices(connection, table.descriptor());
            foreignKeys = ForeignKey.getForeignKeys(connection, table.descriptor());
        } catch (SQLException e) {
            Logger.getLogger("TableComponent").severe("Could not load table " + table.descriptor());
            throw new RuntimeException(e);
        }

        TitledPane root = new TitledPane();
        root.setText(table.descriptor().toString());

    }

}
