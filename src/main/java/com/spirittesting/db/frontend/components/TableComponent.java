package com.spirittesting.db.frontend.components;

import com.spirittesting.db.FxmlView;
import com.spirittesting.db.database.Table;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;

import java.util.Set;
import java.util.logging.Logger;

public class TableComponent extends FxmlView {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Table table;
    private final Set<TableColumnModelItem> columns;

    @FXML
    TitledPane tablePane;
    @FXML
    ListView<TableColumnModelItem> columnsList;

    public TableComponent(Table table) {
        super();
        this.table = table;
        this.columns = TableColumnModelItem.fromTable(table);
    }

    @Override
    public Parent getView() {
        Parent view = super.getView();
        tablePane.setText(table.descriptor().toString());
        columnsList.getItems().addAll(columns);
        view.setOnMousePressed(this::onMousePressed);
        view.setOnMouseDragged(this::onMouseDragged);
        return view;
    }

    public void onMousePressed(MouseEvent event) {
        event.setDragDetect(false);
        Node node = (Node) event.getSource();
        Point2D offset = new Point2D(event.getX() - node.getLayoutX(), event.getY() - node.getLayoutY());
        node.setUserData(offset);
        event.consume();
    }

    public void onMouseDragged(MouseEvent event) {
        event.setDragDetect(false);
        Node node = (Node) event.getSource();
        Point2D offset = (Point2D) node.getUserData();
        node.setLayoutX(event.getX() - offset.getX());
        node.setLayoutY(event.getY() - offset.getY());
        event.consume();
    }

}
