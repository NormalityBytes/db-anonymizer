package com.spirittesting.db.frontend.databaseview;

import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.Table;
import com.spirittesting.db.database.TableId;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;

public class DatabaseViewController implements Initializable {

    @FXML
    TreeView<DatabaseTreeItem> databaseTree;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        databaseTree.setCellFactory(callback -> new TreeCell<>() {
            @Override
            protected void updateItem(DatabaseTreeItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.name);
                    if (item.value != null && !item.value.isBlank()) setTooltip(new Tooltip(item.value));
                    switch (item.type) {
                        case ROOT, TABLE -> setStyle("-fx-text-fill: black");
                        case COLUMN -> setStyle("-fx-text-fill: gray");
                        case INDEX -> setStyle("-fx-text-fill: green");
                        case HAS_DEPENDENCY -> setStyle("-fx-text-fill: blue");
                        case IS_DEPENDENCY -> setStyle("-fx-text-fill: red");
                    }
                }
            }
        });

        createTreeNodes();

    }

    void createTreeNodes() {
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            Collection<Table> tables = Table.getTables(connection, "TABLE");

            TreeItem<DatabaseTreeItem> root = new TreeItem<>(new DatabaseTreeItem("root", "", NodeType.ROOT, new TableId(null, null, null)));
            tables.stream().sorted().forEach(table -> {
                TreeItem<DatabaseTreeItem> tableItem = new TreeItem<>(new DatabaseTreeItem(table.descriptor().table(), table.remarks(), NodeType.TABLE, table.descriptor()));
                root.getChildren().add(tableItem);
            });
            databaseTree.setRoot(root);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    enum NodeType {
        ROOT, TABLE, COLUMN, INDEX, HAS_DEPENDENCY, IS_DEPENDENCY
    }

    record DatabaseTreeItem(String name, String value, NodeType type, TableId tableId) {
    }

}
