package com.spirittesting.db.frontend.cellfactories;

import com.spirittesting.db.database.Table;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.StringJoiner;

public class TableCellFactory implements Callback<ListView<Table>, ListCell<Table>> {

    @Override
    public ListCell<Table> call(ListView<Table> tableListView) {
        return new ListCell<>() {
            @Override
            public void updateItem(Table table, boolean empty) {
                super.updateItem(table, empty);
                if (empty || table == null) {
                    setText(null);
                } else {
                    String label = table.descriptor().table();
                    if (table.descriptor().schema() != null && !table.descriptor().schema().isEmpty()) {
                        label = table.descriptor().schema() + "." + label;
                    }
                    if (table.descriptor().catalog() != null && !table.descriptor().catalog().isEmpty()) {
                        label = table.descriptor().catalog() + "." + label;
                    }
                    setText(label);
                }
            }
        };
    }
}
