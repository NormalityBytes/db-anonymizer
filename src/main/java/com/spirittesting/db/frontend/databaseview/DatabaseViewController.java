package com.spirittesting.db.frontend.databaseview;

import com.spirittesting.db.model.ColumnDefinition;
import com.spirittesting.db.model.ForeignKeyDefinition;
import com.spirittesting.db.model.IndexDefinition;
import com.spirittesting.db.model.Reader;
import com.spirittesting.db.model.TableDefinition;
import com.spirittesting.db.model.TableDescriptor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

public class DatabaseViewController implements Initializable {

    @FXML
    TreeView<DatabaseTreeItem> databaseTree;
    private Reader reader;

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

    }

    void setReader(Reader reader) {
        this.reader = reader;
    }

    void createTreeNodes() {
        Collection<TableDefinition> tableDefinitions = reader.getTableDefinitions();
        Collection<ColumnDefinition> columnDefinitions = reader.getColumnDefinitions();
        TreeItem<DatabaseTreeItem> root = new TreeItem<>(new DatabaseTreeItem("root", "", NodeType.ROOT, new TableDescriptor(null, null, null)));
        tableDefinitions.stream().filter(td -> "TABLE".equals(td.type())).sorted().forEach(tableDefinition -> {
            TreeItem<DatabaseTreeItem> tableItem = new TreeItem<>(new DatabaseTreeItem(tableDefinition.descriptor().table(), tableDefinition.remarks(), NodeType.TABLE, tableDefinition.descriptor()));
            columnDefinitions.stream().filter(columnDefinition -> columnDefinition.descriptor().table().equals(tableDefinition.descriptor())).sorted().forEach(columnDefinition -> {
                TreeItem<DatabaseTreeItem> columnItem = new TreeItem<>(new DatabaseTreeItem(columnDefinition.descriptor().column(), columnDefinition.remarks(), NodeType.COLUMN, columnDefinition.descriptor().table()));
                tableItem.getChildren().add(columnItem);
            });
            root.getChildren().add(tableItem);
        });
        databaseTree.setRoot(root);
    }

    enum NodeType {
        ROOT, TABLE, COLUMN, INDEX, HAS_DEPENDENCY, IS_DEPENDENCY
    }

    record DatabaseTreeItem(String name, String value, NodeType type, TableDescriptor tableDescriptor) {
    }

    void addNodes(TreeItem<DatabaseTreeItem> treeItem) {
        treeItem.getChildren().clear();
        Set<ColumnDefinition> columnDefinitions = reader.getColumnDefinitions(treeItem.getValue().tableDescriptor);
        columnDefinitions.stream().sorted().forEach(columnDefinition -> {
            TreeItem<DatabaseTreeItem> columnItem = new TreeItem<>(new DatabaseTreeItem(columnDefinition.descriptor().column(), columnDefinition.remarks(), NodeType.COLUMN, columnDefinition.descriptor().table()));
            treeItem.getChildren().add(columnItem);
        });

        Set<IndexDefinition> indexDefinitions = reader.getIndexDefinitions(treeItem.getValue().tableDescriptor);
        indexDefinitions.stream().sorted().forEach(indexDefinition -> {
            TreeItem<DatabaseTreeItem> indexItem = new TreeItem<>(new DatabaseTreeItem(indexDefinition.indexName(), indexDefinition.indexQualifier(), NodeType.INDEX, indexDefinition.table()));
            treeItem.getChildren().add(indexItem);
        });

        Set<ForeignKeyDefinition> foreignKeyDefinitions = reader.getForeignKeyDefinitions(treeItem.getValue().tableDescriptor);
        foreignKeyDefinitions.stream().filter(foreignKeyDefinition -> foreignKeyDefinition.primaryKey().table().equals(treeItem.getValue().tableDescriptor)).forEach(foreignKeyDefinition -> {
            TreeItem<DatabaseTreeItem> foreignKeyItem = new TreeItem<>(new DatabaseTreeItem(foreignKeyDefinition.fkName(), foreignKeyDefinition.foreignKey().table() + "." + foreignKeyDefinition.foreignKey().column(), NodeType.HAS_DEPENDENCY, foreignKeyDefinition.primaryKey().table()));
            treeItem.getChildren().add(foreignKeyItem);
        });
        foreignKeyDefinitions.stream().filter(foreignKeyDefinition -> foreignKeyDefinition.foreignKey().table().equals(treeItem.getValue().tableDescriptor)).forEach(foreignKeyDefinition -> {
            TreeItem<DatabaseTreeItem> foreignKeyItem = new TreeItem<>(new DatabaseTreeItem(foreignKeyDefinition.fkName(), foreignKeyDefinition.foreignKey().table() + "." + foreignKeyDefinition.foreignKey().column(), NodeType.IS_DEPENDENCY, foreignKeyDefinition.primaryKey().table()));
            treeItem.getChildren().add(foreignKeyItem);
        });


    }
}
